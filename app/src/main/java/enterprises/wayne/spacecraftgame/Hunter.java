package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 1/1/2016.
 */
public class Hunter extends EnemyEntity {

    // For hunting the player; should be slower than player on x-axis
    private int mWaypointX;
    private long mLastWaypointSetTime;
    private static final int HORIZONTAL_SPEED = 2;
    private static final long WAYPOINT_UPDATE_PERIOD = 500; // in milliseconds

    public Hunter(Context context, int screenX, int screenY) {
        super(context, Type.HUNTER_1, screenX, screenY);

        mWaypointX = 0;
        mLastWaypointSetTime = System.currentTimeMillis();
    }

    /**
     * @param playerPositionX the x-coordinate of the player
     * @post if enough time has passed, waypoint on x-axis
     * has been updated
     */
    public void setWaypointX(int playerPositionX) {
        if (System.currentTimeMillis() >
                mLastWaypointSetTime + WAYPOINT_UPDATE_PERIOD) {
            mLastWaypointSetTime = System.currentTimeMillis();
            mWaypointX = playerPositionX;
        }
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
