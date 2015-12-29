package enterprises.wayne.spacecraftgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Aaron on 12/28/2015.
 */
public abstract class Spacecraft {
    private Bitmap mBitmap;
    private int mX, mY; // position of the spacecraft
    private int mSpeedX, mSpeedY;
    private int mScreenX, mScreenY;

    public enum Type {
        HERO,
    }

    /**
     * @param context to allow access to drawables
     * @param type so that appropriate bitmap can be selected
     * @param screenX user's screen's width (in pixels)
     * @param screenY user's screen's height (in pixels)
     */
    public Spacecraft(Context context, Type type, int screenX, int screenY) {
        // Select appropriate bitmap for the spacecraft
        switch (type) {
            case HERO:
                mBitmap = BitmapFactory.decodeResource
                        (context.getResources(), R.drawable.hero);
                break;
        }

        mX = 50;
        mY = 50;
        mSpeedX = 1;
        mSpeedY = 1;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public int getX() {
        return mX;
    }

    protected void setX(int x) {
        mX = x;
    }

    public int getY() {
        return mY;
    }

    protected void setY(int y) {
        mY = y;
    }

    public int getSpeedX() {
        return mSpeedX;
    }

    protected void setSpeedX(int speed) {
        mSpeedX = speed;
    }

    public int getSpeedY() {
        return mSpeedY;
    }

    protected void setSpeedY(int speed) {
        mSpeedY = speed;
    }

    public int getScreenX() {
        return mScreenX;
    }

    public int getScreenY() {
        return mScreenY;
    }

    public abstract void update();
}
