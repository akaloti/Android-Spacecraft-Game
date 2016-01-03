package enterprises.wayne.spacecraftgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by Aaron on 12/28/2015.
 */
public abstract class Spacecraft {
    private Bitmap mBitmap;
    private int mX, mY; // position of the spacecraft
    private int mMaxX, mMinX; // for keeping craft in boundaries
    private int mSpeedX, mSpeedY;

    private Rect mHitBox; // for collision detection

    protected enum Type {
        HERO,
        ENEMY_1,
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
            case ENEMY_1:
                mBitmap = BitmapFactory.decodeResource
                        (context.getResources(), R.drawable.enemy1);
                break;
            default:
                throw new AssertionError("Invalid type given to Spacecraft()");
        }

        mX = 50;
        mY = 50;
        mSpeedX = 1;
        mSpeedY = 1;

        mMaxX = screenX - mBitmap.getWidth();
        mMinX = 0;

        mHitBox = new Rect(mX, mY, mBitmap.getWidth(), mBitmap.getHeight());
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

    public int getMaxX() {
        return mMaxX;
    }

    public int getMinX() {
        return mMinX;
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

    public Rect getHitBox() {
        return mHitBox;
    }

    /**
     * @pre spacecraft's position has been updated
     * @post location of hit box has been updated
     */
    protected void updateHitBox() {
        mHitBox.left = mX;
        mHitBox.top = mY;
        mHitBox.right = mX + mBitmap.getWidth();
        mHitBox.bottom = mY + mBitmap.getHeight();
    }
}