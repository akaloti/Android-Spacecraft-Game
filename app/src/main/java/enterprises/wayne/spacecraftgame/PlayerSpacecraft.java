package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 12/28/2015.
 */
public class PlayerSpacecraft extends Spacecraft {

    private HorizontalDirection mHorizontalDirection =
            HorizontalDirection.NONE;

    private static final int HORIZONTAL_SPEED = 10;

    public enum HorizontalDirection {
        NONE,
        LEFT,
        RIGHT,
    }

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

    public void setHorizontalDirection(HorizontalDirection dir) {
        mHorizontalDirection = dir;
    }

    public void update() {
        // Update speed and position
        updateHorizontalSpeed();
        setX(getX() + getSpeedX());

        keepSpacecraftOnScreen();
    }

    private void updateHorizontalSpeed() {
        switch (mHorizontalDirection) {
            case NONE:
                setSpeedX(0);
                break;
            case LEFT:
                setSpeedX(-HORIZONTAL_SPEED);
                break;
            case RIGHT:
                setSpeedX(HORIZONTAL_SPEED);
                break;
            default:
                throw new AssertionError(
                        "Somehow invalid player's horizontal direction");
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
