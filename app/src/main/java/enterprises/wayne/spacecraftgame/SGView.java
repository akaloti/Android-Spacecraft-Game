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

    private static final int IDEAL_FRAMES_PER_SECOND = 60;
    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final long SLEEP_TIME_MILLISECONDS =
            MILLISECONDS_PER_SECOND / IDEAL_FRAMES_PER_SECOND;

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
        // Initialize spacecrafts
        mPlayer = new PlayerSpacecraft(mContext, Spacecraft.Type.HERO,
                mScreenX, mScreenY);

        makeNewDustList();
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
    }

    private void draw() {
        if (mHolder.getSurface().isValid()) {
            // Lock the area of memory to draw to
            mCanvas = mHolder.lockCanvas();

            // Rub out the last frame
            mCanvas.drawColor(Color.argb(255, 0, 0, 0));

            // Draw trivial text
            mPaint.setColor(Color.argb(255, 255, 255, 255));
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setTextSize(50);
            mCanvas.drawText("Hello", mScreenX / 2, mScreenY / 2, mPaint);

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

            // Unlock and draw the scene
            mHolder.unlockCanvasAndPost(mCanvas);
        }
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
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                // player lifted finger up
                mPlayer.setHorizontalDirection(
                        PlayerSpacecraft.HorizontalDirection.NONE);
                break;

            case MotionEvent.ACTION_DOWN:
                // player touched screen; determine direction
                PlayerSpacecraft.HorizontalDirection dir;
                if (motionEvent.getX() < mScreenX / 2)
                    dir = PlayerSpacecraft.HorizontalDirection.LEFT;
                else if (motionEvent.getX() > mScreenX / 2)
                    dir = PlayerSpacecraft.HorizontalDirection.RIGHT;
                else {
                    // should be extremely unlikely, but in case
                    dir = PlayerSpacecraft.HorizontalDirection.NONE;
                }
                mPlayer.setHorizontalDirection(dir);
                break;
        }

        return true;
    }
}
