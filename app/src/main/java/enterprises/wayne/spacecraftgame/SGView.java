package enterprises.wayne.spacecraftgame;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Aaron on 12/27/2015.
 */
public class SGView extends SurfaceView
    implements Runnable {

    private final boolean mDebuggingLevels = false;

    private Context mContext;

    private int mScreenX;
    private int mScreenY;

    private volatile boolean mIsPlaying;
    private volatile boolean mShouldRestartGame;
    private Thread mGameThread = null;

    private PlayerSpacecraft mPlayer;
    private ArrayList<EnemyEntity> mEnemyEntities;
    private CopyOnWriteArrayList<EnemyEntityData> mEnemyData;

    private CopyOnWriteArrayList<SpaceDust> mDustList;
    private static final int NUMBER_OF_DUST = 120;

    // For controlling frame rate
    private static final int IDEAL_FRAMES_PER_SECOND = 60;
    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final long SLEEP_TIME_MILLISECONDS =
            MILLISECONDS_PER_SECOND / IDEAL_FRAMES_PER_SECOND;

    private float mForwardDistanceRemaining;
    private float mForwardDistanceGoal;

    private boolean mFinishedGame = false;
    private boolean mFinishedLevel = false;
    private boolean mLost = false;

    // for allowing the user to see win/loss screen for brief time
    private long mGameEndTime;
    private static final long GAME_END_WAIT_MILLISECONDS = 1000;

    private SoundPool mSoundPool;
    private int mStartSound = -1;
    private int mWinSound = -1;
    private int mLossSound = -1;

    private MediaPlayer mBackgroundMusic;
    private int mCurrentMusic = -1;

    private ArrayList<LevelData> mLevels;
    private LevelData mCurrentLevel;
    private int mLevelIndex;

    // For drawing
    private Paint mPaint;
    private Canvas mCanvas;
    private SurfaceHolder mHolder; // for locking the canvas

    public SGView(Context context, int screenX, int screenY) {
        super(context);

        mContext = context;

        // do this early to minimize the chance that the sounds
        // don't get loaded in time
        loadSounds();

        mScreenX = screenX;
        mScreenY = screenY;

        mHolder = getHolder();
        mPaint = new Paint();

        mEnemyEntities = new ArrayList<EnemyEntity>();
        mEnemyData = new CopyOnWriteArrayList<EnemyEntityData>();
        mDustList = new CopyOnWriteArrayList<SpaceDust>();

        restartGame();
        makeNewDustList();
    } // SGView constructor

    /**
     * @post all sound effects have been loaded (in an order intended
     * to minimize the possibility of an unloaded sound being played)
     */
    private void loadSounds() {
        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {
            AssetManager assetManager = mContext.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("start.ogg");
            mStartSound = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("win.ogg");
            mWinSound = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("loss.ogg");
            mLossSound = mSoundPool.load(descriptor, 0);
        }
        catch (IOException e) {
            Log.e("error", "failed to load sound files");
        }
    }

    /**
     * @post data for appropriate levels (depending on whether debugging
     * or not) has been set up
     */
    private void initializeLevelData() {
        mLevels = new ArrayList<>();

        if (mDebuggingLevels) {
            // Level 1
            LevelData levelData = new LevelData();
            levelData.goalDistance = 1200;
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.BIG_ASTEROID, 0, 1400));
            levelData.backgroundMusicResId = R.raw.king_galaxian;
            mLevels.add(levelData);

            /*// Level 2
            levelData = new LevelData();
            levelData.goalDistance = 200;
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.HUNTER_1, 0, 1000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.HUNTER_1, 0, 1000));
            levelData.backgroundMusicResId = R.raw.king_galaxian;
            mLevels.add(levelData);

            // Level 3
            levelData = new LevelData();
            levelData.goalDistance = 200;
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.SMALL_ASTEROID, 0, 1000));
            levelData.backgroundMusicResId = R.raw.saber_wing;
            mLevels.add(levelData);

            // Level 4
            levelData = new LevelData();
            levelData.goalDistance = 400;
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.HUNTER_1, 0, 1000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.HUNTER_1, 0, 1000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.HUNTER_1, 200, 1000));
            levelData.backgroundMusicResId = R.raw.saber_wing;
            mLevels.add(levelData);*/
        }
        else { // not debugging -- use real levels
            // Level 1
            LevelData levelData = new LevelData();
            levelData.goalDistance = 600;
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.DUMMY_1, 0, 1000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.DUMMY_1, 200, 1000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.DUMMY_1, 400, 1000));
            levelData.backgroundMusicResId = R.raw.king_galaxian;
            mLevels.add(levelData);

            // Level 2
            levelData = new LevelData();
            levelData.goalDistance = 700;
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.SMALL_ASTEROID, 0, 1000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.DUMMY_1, 0, 1000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.DUMMY_1, 50, 1000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.HUNTER_1, 300, 1000));
            levelData.backgroundMusicResId = R.raw.king_galaxian;
            mLevels.add(levelData);

            // Level 3
            levelData = new LevelData();
            levelData.goalDistance = 800;
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.SMALL_ASTEROID, 0, 1000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.SMALL_ASTEROID, 20, 1000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.SMALL_ASTEROID, 40, 1000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.SMALL_ASTEROID, 60, 1000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.SMALL_ASTEROID, 100, 1000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.SMALL_ASTEROID, 200, 1000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.SMALL_ASTEROID, 400, 1000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.SMALL_ASTEROID, 600, 1000));
            levelData.backgroundMusicResId = R.raw.king_galaxian;
            mLevels.add(levelData);

            // Level 4
            levelData = new LevelData();
            levelData.goalDistance = 1000;
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.SMALL_ASTEROID, 0, 2000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.SMALL_ASTEROID, 0, 2000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.HUNTER_1, 0, 2000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.HUNTER_1, 50, 2000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.DUMMY_1, 200, 2000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.HUNTER_1, 600, 2000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.DUMMY_1, 850, 2000));
            levelData.backgroundMusicResId = R.raw.king_galaxian;
            mLevels.add(levelData);

            // Level 5
            levelData = new LevelData();
            levelData.goalDistance = 1500;
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.HUNTER_1, 0, 2000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.HUNTER_1, 200, 2000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.HUNTER_1, 400, 2000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.HUNTER_1, 600, 2000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.HUNTER_1, 800, 2000));
            levelData.enemyData.add(
                    new EnemyEntityData(Entity.Type.HUNTER_1, 1000, 2000));
            levelData.backgroundMusicResId = R.raw.saber_wing;
            mLevels.add(levelData);
        }
    }

    /**
     * @post each enemy in mEnemyData whose start distance has been reached
     * has been constructed
     */
    private void spawnEnemies() {
        float distanceTravelled =
                mForwardDistanceGoal - mForwardDistanceRemaining;
        for (EnemyEntityData eed : mEnemyData) {
            // Has the player travelled far enough for this enemy to spawn?
            if (!eed.hasSpawned && distanceTravelled >= eed.startDistance) {
                // Create this enemy

                switch (eed.type) {
                    case DUMMY_1:
                        mEnemyEntities.add(
                                new Dummy(mContext, mScreenX, mScreenY,
                                          eed.endDistance));
                        break;
                    case BIG_DUMMY_1:
                        mEnemyEntities.add(
                                new BigDummy1(mContext, mScreenX, mScreenY,
                                        eed.endDistance));
                        break;
                    case HUNTER_1:
                        mEnemyEntities.add(
                                new Hunter(mContext, mScreenX, mScreenY,
                                           eed.endDistance));
                        break;
                    case BIG_HUNTER_1:
                        mEnemyEntities.add(
                                new BigHunter1(mContext, mScreenX, mScreenY,
                                        eed.endDistance));
                        break;
                    case ZIGZAG_1:
                        mEnemyEntities.add(
                                new Zigzag(mContext, mScreenX, mScreenY,
                                        eed.endDistance));
                        break;
                    case AMBUSHER_1:
                        mEnemyEntities.add(
                                new Ambusher(mContext, mScreenX, mScreenY,
                                        eed.endDistance));
                        break;
                    case SMALL_ASTEROID:
                        mEnemyEntities.add(
                                new SmallAsteroid(mContext,
                                        mScreenX, mScreenY,
                                        eed.endDistance));
                        break;
                    case BIG_ASTEROID:
                        mEnemyEntities.add(
                                new BigAsteroid(mContext,
                                        mScreenX, mScreenY,
                                        eed.endDistance));
                        break;
                    default:
                        throw new AssertionError(
                                "Spawned enemy has invalid type");
                }

                eed.hasSpawned = true;
            }
        }
    }

    /**
     * @post each enemy in mEnemyEntities whose end distance has been
     * reached by the player has been destroyed
     */
    private void despawnEnemies() {
        float distanceTravelled =
                mForwardDistanceGoal - mForwardDistanceRemaining;
        for (Iterator<EnemyEntity> itr = mEnemyEntities.iterator();
             itr.hasNext(); ) {
            // Mark correct enemies for removal
            EnemyEntity enemy = itr.next();
            if (!enemy.isMarkedForRemoval() &&
                    distanceTravelled >= enemy.getEndDistance())
                enemy.markForRemoval();
        }
    }

    /**
     * @param id of the new background music
     * @pre mCurrentLevel is updated
     * @post mBackgroundMusic has been appropriately changed;
     * old music was stopped; new one was started
     */
    private void changeBackgroundMusic() {
        // Only restart the background music if there really is a change
        if (mCurrentMusic != mCurrentLevel.backgroundMusicResId) {
            if (mBackgroundMusic != null) {
                // Remove old background music
                mBackgroundMusic.stop();
                mBackgroundMusic.release();
            }

            // Start new background music
            mCurrentMusic = mCurrentLevel.backgroundMusicResId;
            mBackgroundMusic = MediaPlayer.create(mContext, mCurrentMusic);
            mBackgroundMusic.setLooping(true);
            mBackgroundMusic.setVolume(1.0f, 1.0f);
            mBackgroundMusic.start();
        }
    }

    /**
     * @post game has been restarted, with appropriate level change
     * (if any)
     */
    private void restartGame() {
        if (mFinishedGame) {
            // if game was finished, user restarts
            mLevelIndex = 0;

            // to set enemy data to "unspawned"
            initializeLevelData();

            mCurrentLevel = mLevels.get(mLevelIndex);
            changeBackgroundMusic();
        }
        else if (mFinishedLevel) {
            // user goes to next level
            ++mLevelIndex;
            mCurrentLevel = mLevels.get(mLevelIndex);
            changeBackgroundMusic();
        }
        else if (mLost) {
            // to set enemy data to "unspawned", to restart
            // current level's enemy data
            initializeLevelData();

            mCurrentLevel = mLevels.get(mLevelIndex);
        }
        else { // first time playing game
            initializeLevelData();
            mCurrentLevel = mLevels.get(mLevelIndex);
            changeBackgroundMusic();
        }

        mShouldRestartGame = false;
        mFinishedGame = mFinishedLevel = mLost = false;

        mForwardDistanceGoal = mCurrentLevel.goalDistance;
        mForwardDistanceRemaining = mForwardDistanceGoal;

        // Reset player and enemies
        mEnemyData = mCurrentLevel.enemyData;
        mPlayer = new PlayerSpacecraft(mContext, mScreenX, mScreenY);
        mEnemyEntities.clear();
        spawnEnemies(); // do after resetting remaining distance

        // Is at end of function so is more likely to play the first time
        mSoundPool.play(mStartSound, 1, 1, 0, 0, 1);
    }

    private void makeNewDustList() {
        mDustList.clear();
        for (int i = 0; i < NUMBER_OF_DUST; i++) {
            SpaceDust speck = new SpaceDust(mScreenX, mScreenY);
            mDustList.add(speck);
        }
    }

    @Override
    public void run() {
        while (mIsPlaying) {
            update();
            draw();
            controlFrameRate();

            if (gameEnded() && mShouldRestartGame)
                restartGame();
        }
    }

    private void update() {
        // Check for collision only if isn't the first frame
        // (since everyone's hit box is at default spot) and if
        // game hasn't ended
        if (!gameEnded()) {
            if (isCollision())
                resolveLoss();
        }

        // should come before updating the enemies, since their
        // vertical speeds depends on player's
        mPlayer.update();

        int playerCenterX = mPlayer.getCenterX();
        int playerSpeedX = mPlayer.getSpeedX();
        int playerSpeedY = mPlayer.getSpeedY();
        updateEnemies(playerCenterX, playerSpeedX, playerSpeedY);

        // Update each speck of dust
        for (SpaceDust sd : mDustList)
            sd.update(playerSpeedY);

        if (!gameEnded())
            updateRemainingDistance();
    }

    /**
     * @return true if the player is colliding with any enemy;
     * false, otherwise
     */
    private boolean isCollision() {
        Rect playerHitBox = mPlayer.getHitBox();

        // Check each enemy
        for (EnemyEntity es : mEnemyEntities) {
            if (Rect.intersects(playerHitBox, es.getHitBox()))
                return true;
        }

        return false;
    }

    /**
     * @post each existing enemy has been updated, and new ones have
     * been spawnwed if player travelled far enough
     * @param playerCenterX
     * @param playerSpeedX
     * @param playerSpeedY
     */
    private void updateEnemies(int playerCenterX, int playerSpeedX,
                               int playerSpeedY) {
        for (Iterator<EnemyEntity> itr = mEnemyEntities.iterator();
             itr.hasNext(); ) {
            EnemyEntity enemy = itr.next();

            // Update waypoint depending on enemy type
            if (enemy.isHunter()) {
                ((WaypointEnemy) enemy).setWaypointX(playerCenterX);
            }
            else if (enemy.isAmbusher()) {
                int waypoint = ((Ambusher) enemy).determineOffset(
                        playerCenterX, playerSpeedX);
                ((WaypointEnemy) enemy).setWaypointX(waypoint);
            }

            boolean shouldDestroy = !(enemy.update(playerSpeedY));
            if (shouldDestroy)
                itr.remove();
        }

        if (!gameEnded()) {
            spawnEnemies();
            despawnEnemies();
        }
    }

    /**
     * @post remaining forward distance has been updated; if user has
     * won, game is notified
     */
    private void updateRemainingDistance() {
        mForwardDistanceRemaining -= mPlayer.getSpeedY();

        if (mForwardDistanceRemaining < 0) {
            resolveWin();
        }
    }

    /**
     * @post user's having lost has been reacted to; game has been
     * notified so that appropriate message will be drawn
     */
    private void resolveLoss() {
        mSoundPool.play(mLossSound, 1, 1, 0, 0, 1);
        mLost = true;

        mGameEndTime = System.currentTimeMillis();
    }

    /**
     * @post user's having won has been reacted to; game has
     * determined if user beat the entire game
     */
    private void resolveWin() {
        mSoundPool.play(mWinSound, 1, 1, 0, 0, 1);
        mEnemyEntities.clear();

        if ((mLevelIndex + 1) == mLevels.size())
            mFinishedGame = true;
        else
            mFinishedLevel = true;

        mGameEndTime = System.currentTimeMillis();
    }

    private void draw() {
        if (mHolder.getSurface().isValid()) {
            // Lock the area of memory to draw to
            mCanvas = mHolder.lockCanvas();

            // Rub out the last frame
            mCanvas.drawColor(Color.argb(255, 0, 0, 0));

            // Draw each speck of dust
            mPaint.setColor(SpaceDust.COLOR);
            for (SpaceDust sd : mDustList)
                    mCanvas.drawPoint(sd.getX(), sd.getY(), mPaint);

            // For debugging
            // drawHitBoxes();

            // Only draw the player if hasn't lost
            if (!mLost)
                mCanvas.drawBitmap(
                        mPlayer.getBitmap(),
                        mPlayer.getX(),
                        mPlayer.getY(),
                        mPaint);

            // Draw each enemy
            for (EnemyEntity es : mEnemyEntities)
                mCanvas.drawBitmap(es.getBitmap(), es.getX(),
                        es.getY(), mPaint);

            if (!gameEnded())
                drawHUD();
            else {
                if (mFinishedGame)
                    drawFinishedGameScreen();
                else if (mFinishedLevel)
                    drawFinishedLevelScreen();
                else if (mLost)
                    drawLossScreen();
            }

            // Unlock and draw the scene
            mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    /**
     * This function is for debugging.
     *
     * @post each spacecraft's hit box has been drawn
     */
    private void drawHitBoxes() {
        Rect hitBox;
        mPaint.setColor(Color.argb(255, 255, 255, 255));

        // draw player's hit box
        hitBox = mPlayer.getHitBox();
        mCanvas.drawRect(hitBox.left, hitBox.top, hitBox.right,
                hitBox.bottom, mPaint);

        // draw each enemy's hit box
        for (EnemyEntity es : mEnemyEntities) {
            hitBox = es.getHitBox();
            mCanvas.drawRect(hitBox.left, hitBox.top, hitBox.right,
                    hitBox.bottom, mPaint);
        }
    }

    /**
     * @post the heads-up display has been drawn
     */
    private void drawHUD() {
        mPaint.setColor(Color.argb(255, 255, 255, 255));

        // draw current level number (not index); note that the
        // third parameter of drawText(...) is for text's bottom Y
        int levelTextSize = 25;
        mPaint.setTextSize(levelTextSize);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mCanvas.drawText("Level: " + (mLevelIndex + 1),
                20, levelTextSize, mPaint);

        // draw forward distance remaining
        int distanceTextSize = 25;
        mPaint.setTextSize(distanceTextSize);
        mPaint.setTextAlign(Paint.Align.RIGHT);
        mCanvas.drawText("Remaining: " + mForwardDistanceRemaining +
            " meters", mScreenX - 20, distanceTextSize, mPaint);
    }

    private void drawFinishedGameScreen() {
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(60);
        mPaint.setColor(Color.RED);
        mCanvas.drawText("Game finished!", mScreenX / 2, mScreenY / 2, mPaint);
        mPaint.setColor(Color.WHITE);

        mPaint.setTextSize(40);
        mCanvas.drawText("Tap to restart game.", mScreenX / 2,
                mScreenY / 2 + 60, mPaint);
    }

    private void drawFinishedLevelScreen() {
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(60);
        mCanvas.drawText("Level Completed!",
                mScreenX / 2, (mScreenY / 2) - 50, mPaint);

        mPaint.setTextSize(30);
        mCanvas.drawText("Completed: " + mForwardDistanceGoal,
                mScreenX / 2, mScreenY / 2 + 10, mPaint);

        mPaint.setTextSize(40);
        mPaint.setColor(Color.RED);
        mCanvas.drawText("Tap to advance.", mScreenX / 2,
                mScreenY / 2 + 100, mPaint);
    }

    private void drawLossScreen() {
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(60);
        mCanvas.drawText("You lost... :(", mScreenX / 2,
                (mScreenY / 2) - 50, mPaint);

        mPaint.setTextSize(30);
        mCanvas.drawText("Remaining: " + mForwardDistanceRemaining,
                mScreenX / 2, (mScreenY / 2) + 10, mPaint);

        mPaint.setTextSize(40);
        mPaint.setColor(Color.RED);
        mCanvas.drawText("Tap to restart level.", mScreenX / 2,
                (mScreenY / 2) + 100, mPaint);
    }

    private void controlFrameRate() {
        try {
            mGameThread.sleep(SLEEP_TIME_MILLISECONDS);
        }
        catch (InterruptedException e) {

        }
    }

    public void pause() {
        mIsPlaying = false;

        // I had to decide whether to allow the user to be able to pause
        // or not (and thus whether to call pause() or release()); thus,
        // this risks that the MusicPlayer could drain the player's battery
        // if he leaves this app paused
        mBackgroundMusic.pause();

        try {
            // Clean up thread
            mGameThread.join();
        } catch (InterruptedException e) {

        }
    }

    public void resume() {
        mIsPlaying = true;
        mBackgroundMusic.start();
        mGameThread = new Thread(this);
        mGameThread.start();
    }

    public void stop() {
        mBackgroundMusic.release();
    }

    /**
     * @param motionEvent
     * @post if game hasn't ended, the player spacecraft has been
     * told to move based on where the player is touching the screen;
     * if game has ended, user's touch restarts the game
     * @return true, so as to not resolve this motionEvent
     * any further
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!gameEnded()) {
            /**
             * Game hasn't ended, so probably move player's spacecraft
             */

            boolean left = false;
            boolean right = false;

            int pointerCount = motionEvent.getPointerCount();
            for (int i = 0; i < pointerCount; ++i) {
                int x = (int) motionEvent.getX(i);

                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        // determine which half of the screen is being touched
                        if (x < mScreenX / 2) {
                            // left half, so move left
                            left = true;
                        } else {
                            // right half, so move right
                            right = true;
                        }
                        break;
                }
            }

            mPlayer.setPressingLeft(left);
            mPlayer.setPressingRight(right);
        }
        else {
            mPlayer.setPressingLeft(false);
            mPlayer.setPressingRight(false);

            /**
             * Game has ended, so could restart game (if enough
             * time has passed)
             */
            if (System.currentTimeMillis() >
                    mGameEndTime + GAME_END_WAIT_MILLISECONDS) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mShouldRestartGame = true;
                        break;
                }
            }
        }

        // Either way, the touch event is resolved
        return true;
    }

    private boolean gameEnded() {
        return mFinishedGame || mFinishedLevel || mLost;
    }
}
