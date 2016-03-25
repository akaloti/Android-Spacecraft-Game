package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 12/28/2015.
 */
public class PlayerSpacecraft extends Entity {
    // Regarding user input
    private boolean mIsPressingRight = false;
    private boolean mIsPressingLeft = false;

    private int mMaxSpeedX = 0;
    private int mMaxSpeedY = 0;

    /**
     * @param context to allow access to drawables
     * @param screenX user's screen's width (in pixels)
     * @param screenY user's screen's height (in pixels)
     */
    public PlayerSpacecraft(Context context, int screenX, int screenY) {
        super(context, MainActivity.getChosenSpacecraft(), screenX, screenY);

        setX(screenX / 2);
        setY(screenY - 300);

        mMaxSpeedY = decideMaxSpeedY();
        mMaxSpeedX = decideMaxSpeedX();

        setSpeedY(mMaxSpeedY);
    }

    public void setPressingRight(boolean isPressingRight) {
        mIsPressingRight = isPressingRight;
    }

    public void setPressingLeft(boolean isPressingLeft) {
        mIsPressingLeft = isPressingLeft;
    }

    private int decideMaxSpeedY() {
        switch (getType()) {
            case HERO_1:
                return 2;
            case HERO_2:
                return 4;
            case HERO_3:
                return 1;
            default:
                throw new AssertionError("Invalid spacecraft type");
        }
    }

    private int decideMaxSpeedX() {
        switch (getType()) {
            case HERO_1:
                return 10;
            case HERO_2:
                return 5;
            case HERO_3:
                return 15;
            default:
                throw new AssertionError("Invalid spacecraft type");
        }
    }

    public void update() {
        // Update speed and position
        setX(getX() + updateSpeedX());

        keepSpacecraftOnScreen();

        updateHitBox();
    }

    /**
     * @return the new horizontal speed
     */
    private int updateSpeedX() {
        int newSpeed;

        if (mIsPressingLeft && mIsPressingRight) {
            // left and right cancel each other out
            newSpeed = 0;
        }
        else if (mIsPressingLeft)
            newSpeed = -mMaxSpeedX;
        else if (mIsPressingRight)
            newSpeed = mMaxSpeedX;
        else {
            // neither going left nor right
            newSpeed = 0;
        }

        setSpeedX(newSpeed);
        return newSpeed;
    }

    /**
     * @post if spacecraft was horizontally off bounds, its position
     * has been fixed
     */
    private void keepSpacecraftOnScreen() {
        int x = getX();
        int minX = getMinX();
        int maxX = getMaxX();

        if (x < minX)
            setX(minX);
        if (x > maxX)
            setX(maxX);
    }
}
