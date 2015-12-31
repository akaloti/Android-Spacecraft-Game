package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 12/28/2015.
 */
public class PlayerSpacecraft extends Spacecraft {
    private static final int HORIZONTAL_SPEED = 10;

    // Regarding user input
    private boolean mIsPressingRight = false;
    private boolean mIsPressingLeft = false;

    /**
     * @param context to allow access to drawables
     * @param type so that appropriate bitmap can be selected
     * @param screenX user's screen's width (in pixels)
     * @param screenY user's screen's height (in pixels)
     */
    public PlayerSpacecraft(Context context, Type type,
                            int screenX, int screenY) {
        super(context, type, screenX, screenY);

        setX(screenX / 2);
        setY(screenY - 300);
    }

    public void setPressingRight(boolean isPressingRight) {
        mIsPressingRight = isPressingRight;
    }

    public void setPressingLeft(boolean isPressingLeft) {
        mIsPressingLeft = isPressingLeft;
    }

    public void update() {
        // Update speed and position
        updateHorizontalSpeed();
        setX(getX() + getSpeedX());

        keepSpacecraftOnScreen();
    }

    private void updateHorizontalSpeed() {
        if (mIsPressingLeft && mIsPressingRight) {
            // left and right cancel each other out
            setSpeedX(0);
        }
        else if (mIsPressingLeft)
            setSpeedX(-HORIZONTAL_SPEED);
        else if (mIsPressingRight)
            setSpeedX(HORIZONTAL_SPEED);
        else {
            // neither going left nor right
            setSpeedX(0);
        }
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
