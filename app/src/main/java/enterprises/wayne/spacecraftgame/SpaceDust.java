package enterprises.wayne.spacecraftgame;

import android.graphics.Color;

import java.util.Random;

/**
 * Based on same-named class from source code of book "Android Game
 * Programming by Example" by John Horton.
 */
public class SpaceDust {

    private int mX, mY; // position
    private int mSpeedY;
    private final int SPEED_UPPER_BOUND = 10; // is one above max speed

    // For detecting when dust leaves the screen
    private int mMaxX;
    private int mMaxY;
    private int mMinX;
    private int mMinY;

    public final static int COLOR = Color.argb(255, 255, 255, 255);

    /**
     * @param screenX user's screen width (in pixels)
     * @param screenY user's screen height (in pixels)
     */
    public SpaceDust(int screenX, int screenY) {
        mMaxX = screenX;
        mMaxY = screenY;
        mMinX = 0;
        mMinY = 0;

        // Set starting speed and position
        Random generator = new Random();
        mSpeedY = generator.nextInt(SPEED_UPPER_BOUND);
        mX = generator.nextInt(mMaxX);
        mY = generator.nextInt(mMaxY);
    }

    /**
     * @param playerSpeedY
     */
    public void update(int playerSpeedY) {
        // Speed up when player does (so player never technically moves)
        mY -= playerSpeedY;
        mY -= mSpeedY;

        // respawn space dust that leaves boundaries
        if (mY < 0) {
            mY = mMaxY;

            // Pick random speed and position
            Random generator = new Random();
            mX = generator.nextInt(mMaxX);
            mSpeedY = generator.nextInt(SPEED_UPPER_BOUND);
        }
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }
}
