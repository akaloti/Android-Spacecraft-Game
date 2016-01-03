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

/**
 * Created by Aaron on 12/27/2015.
 */
public class SGView extends SurfaceView
    implements Runnable {

    private Context mContext;

    private int mScreenX;
    private int mScreenY;

    private volatile boolean mIsPlaying;
    private Thread mGameThread = null;

    private PlayerSpacecraft mPlayer;
    private ArrayList<EnemySpacecraft> mEnemies;

    private ArrayList<SpaceDust> mDustList;
    private static final int NUMBER_OF_DUST = 120;

    // For controlling frame rate
    private static final int IDEAL_FRAMES_PER_SECOND = 60;
    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final long SLEEP_TIME_MILLISECONDS =
            MILLISECONDS_PER_SECOND / IDEAL_FRAMES_PER_SECOND;

    private float mForwardDistanceRemaining;
    private static final float FORWARD_DISTANCE_GOAL = 600;

    private boolean mWon;
    private boolean mLost;

    // for avoiding certain actions (e.g. collision detection) on first frame
    private boolean mIsFirstFrame;

    // for allowing the user to see win/loss screen for brief time
    private long mGameEndTime;
    private static final long GAME_END_WAIT_MILLISECONDS = 1000;

    private SoundPool mSoundPool;
    int mStartSound = -1;
    int mWinSound = -1;
    int mLossSound = -1;

    MediaPlayer mMediaPlayer;

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

        // Set up background music
        mMediaPlayer = MediaPlayer.create(context, R.raw.background);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.setVolume(1.0f, 1.0f);
        mMediaPlayer.start();

        mScreenX = screenX;
        mScreenY = screenY;

        mHolder = getHolder();
        mPaint = new Paint();

        mEnemies = new ArrayList<EnemySpacecraft>();
        mDustList = new ArrayList<SpaceDust>();

        restartGame();
    }

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

    private void restartGame() {
        mSoundPool.play(mStartSound, 1, 1, 0, 0, 1);

        // Reset player and enemies
        mEnemies.clear();
        initializeSpacecrafts();

        makeNewDustList();

        mForwardDistanceRemaining = FORWARD_DISTANCE_GOAL;
        mWon = mLost = false;
        mIsFirstFrame = true;
    }

    private void initializeSpacecrafts() {
        mPlayer = new PlayerSpacecraft(mContext, mScreenX, mScreenY);
        mEnemies.add(new Dummy(mContext, mScreenX, mScreenY));
        mEnemies.add(new Dummy(mContext, mScreenX, mScreenY));
        mEnemies.add(new Hunter(mContext, mScreenX, mScreenY));
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
        }
    }

    private void update() {
        // Check for collision only if isn't the first frame
        // (since everyone's hit box is at default spot) and if
        // game hasn't ended
        if (!gameEnded() && !mIsFirstFrame) {
            if (isCollision())
                resolveLoss();
        }
        else
            mIsFirstFrame = false;

        mPlayer.update();

        int playerCenterX = mPlayer.getCenterX();
        int playerSpeedY = mPlayer.getSpeedY();
        updateEnemies(playerCenterX, playerSpeedY);

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
        for (EnemySpacecraft es : mEnemies) {
            if (Rect.intersects(playerHitBox, es.getHitBox()))
                return true;
        }

        return false;
    }

    /**
     * @param playerCenterX
     * @param playerSpeedY
     */
    private void updateEnemies(int playerCenterX, int playerSpeedY) {
        for (EnemySpacecraft es : mEnemies) {
            // Update waypoint if hunter
            if (es.isHunter()) {
                ((Hunter) es).setWaypointX(playerCenterX);
            }

            es.update(playerSpeedY);
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
     * @post user's having won has been reacted to; game has been
     * notified so that appropriate message is drawn; enemies have
     * been cleared
     */
    private void resolveWin() {
        mSoundPool.play(mWinSound, 1, 1, 0, 0, 1);
        mWon = true;
        mEnemies.clear();

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
            for (EnemySpacecraft es : mEnemies)
                mCanvas.drawBitmap(es.getBitmap(), es.getX(),
                        es.getY(), mPaint);

            if (!gameEnded())
                drawHUD();
            else {
                if (mWon)
                    drawWinScreen();
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
        for (EnemySpacecraft es : mEnemies) {
            hitBox = es.getHitBox();
            mCanvas.drawRect(hitBox.left, hitBox.top, hitBox.right,
                    hitBox.bottom, mPaint);
        }
    }

    /**
     * @post the heads-up display has been drawn
     */
    private void drawHUD() {
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.argb(255, 255, 255, 255));

        // draw forward distance remaining
        int distanceTextSize = 25;
        mPaint.setTextSize(distanceTextSize);
        mCanvas.drawText("Remaining: " + mForwardDistanceRemaining +
            " meters", mScreenX / 2, distanceTextSize + 5, mPaint);
    }

    private void drawWinScreen() {
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(60);
        mCanvas.drawText("You won!", mScreenX / 2, mScreenY / 2, mPaint);

        mPaint.setTextSize(40);
        mCanvas.drawText("Tap to restart.", mScreenX / 2,
                (mScreenY / 2) + 60, mPaint);
    }

    private void drawLossScreen() {
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(60);
        mCanvas.drawText("You lost... :(", mScreenX / 2, mScreenY / 2, mPaint);

        mPaint.setTextSize(40);
        mCanvas.drawText("Tap to restart.", mScreenX / 2,
                (mScreenY / 2) + 60, mPaint);
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
        try {
            // Clean up thread
            mGameThread.join();
        } catch (InterruptedException e) {

        }
    }

    public void resume() {
        mIsPlaying = true;
        mGameThread = new Thread(this);
        mGameThread.start();
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
            /**
             * Game hasn't ended, so could restart game (if enough
             * time has passed)
             */
            if (System.currentTimeMillis() >
                    mGameEndTime + GAME_END_WAIT_MILLISECONDS) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        restartGame();
                        break;
                }
            }
        }

        // Either way, the touch event is resolved
        return true;
    }

    private boolean gameEnded() {
        return mWon || mLost;
    }
}
