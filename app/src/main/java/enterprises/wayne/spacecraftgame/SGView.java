package enterprises.wayne.spacecraftgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Aaron on 12/27/2015.
 */
public class SGView extends SurfaceView
    implements Runnable {

    private Context mContext;

    private int mScreenX;
    private int mScreenY;

    volatile boolean mIsPlaying;
    Thread mGameThread = null;

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

        drawTrivialText();
    }

    @Override
    public void run() {

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
}
