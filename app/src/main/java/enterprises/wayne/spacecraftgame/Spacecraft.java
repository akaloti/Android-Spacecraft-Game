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
    private int mMaxX, mMinX; // for keeping craft in boundaries
    private int mSpeedX, mSpeedY;

    private Type mType;

    protected enum Type {
        HERO,

        // enemies
        ENEMY_1,
        HUNTER_1,
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
            case HUNTER_1:
                mBitmap = BitmapFactory.decodeResource
                        (context.getResources(), R.drawable.hunter1);
                break;
            default:
                throw new AssertionError("Invalid type given to Spacecraft()");
        }

        mType = type;

        mX = 50;
        mY = 50;
        mSpeedX = 0;
        mSpeedY = 0;

        mMaxX = screenX - mBitmap.getWidth();
        mMinX = 0;
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

    public Type getType() {
        return mType;
    }

    public boolean isHunter() {
        return mType == Type.HUNTER_1;
    }
}
