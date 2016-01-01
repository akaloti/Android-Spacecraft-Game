package enterprises.wayne.spacecraftgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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

    private ArrayList<SpaceDust> mDustList = new ArrayList<SpaceDust>();
    private static final int NUMBER_OF_DUST = 120;

    // For controlling frame rate
    private static final int IDEAL_FRAMES_PER_SECOND = 60;
    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final long SLEEP_TIME_MILLISECONDS =
            MILLISECONDS_PER_SECOND / IDEAL_FRAMES_PER_SECOND;

    private float mForwardDistanceRemaining;
    private static final float FORWARD_DISTANCE_GOAL = 1000;

    private boolean mWon;
    private boolean mLost;

    // For drawing
    private Paint mPaint;
    private Canvas mCanvas;
    private SurfaceHolder mHolder; // for locking the canvas

    public SGView(Context context, int screenX, int screenY) {
        super(context);

        mContext = context;

        mScreenX = screenX;
        mScreenY = screenY;

        mHolder = getHolder();
        mPaint = new Paint();

        restartGame();
    }

    private void restartGame() {
        initializeSpacecrafts();
        makeNewDustList();
        mForwardDistanceRemaining = FORWARD_DISTANCE_GOAL;
        mWon = mLost = false;
    }

    private void initializeSpacecrafts() {
        mPlayer = new PlayerSpacecraft(mContext, Spacecraft.Type.HERO,
                mScreenX, mScreenY);
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
        mPlayer.update();

        // Update each speck of dust
        for (SpaceDust sd : mDustList)
            sd.update(mPlayer.getSpeedY());

        updateRemainingDistance();
    }

    /**
     * @post remaining forward distance has been updated; if user has
     * won, he'll be told this
     */
    private void updateRemainingDistance() {
        mForwardDistanceRemaining -= mPlayer.getSpeedY();
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

            // Draw the player
            mCanvas.drawBitmap(
                    mPlayer.getBitmap(),
                    mPlayer.getX(),
                    mPlayer.getY(),
                    mPaint);

            if (!gameEnded())
                drawHUD();

            // Unlock and draw the scene
            mHolder.unlockCanvasAndPost(mCanvas);
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

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
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
                    }
                    else {
                        // right half, so move right
                        right = true;
                    }
                    break;
            }
        }

        mPlayer.setPressingLeft(left);
        mPlayer.setPressingRight(right);

        return true;
    }

    private boolean gameEnded() {
        return mWon || mLost;
    }
}
