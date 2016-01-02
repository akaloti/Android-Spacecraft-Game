package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 1/1/2016.
 */
public class Hunter extends EnemySpacecraft {

    // For hunting the player; should be slower than player on x-axis
    private int mWaypointX;
    private static final int HORIZONTAL_SPEED = 2;

    public Hunter(Context context, int screenX, int screenY) {
        super(context, Type.HUNTER_1, screenX, screenY);

        mWaypointX = 0;
    }

    /**
     * @param playerPositionX the x-coordinate of the player
     */
    public void setWaypointX(int playerPositionX) {
        mWaypointX = playerPositionX;
    }

    /**
     * @param playerSpeedY is used to move the enemy further,
     * creating the illusion that the player is moving forward
     * @post enemy has been moved vertically, and has been moved
     * horizontally towards waypoint
     */
    @Override
    public void update(int playerSpeedY) {
        // Move enemy towards waypoint
        int centerX = getCenterX();
        if (mWaypointX < centerX)
            setSpeedX(-HORIZONTAL_SPEED);
        else if (mWaypointX > centerX)
            setSpeedX(HORIZONTAL_SPEED);
        else
            setSpeedX(0);

        super.update(playerSpeedY);
    }
}
