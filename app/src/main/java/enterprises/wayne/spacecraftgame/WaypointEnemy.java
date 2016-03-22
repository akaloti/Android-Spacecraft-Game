package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 3/22/2016.
 */
public class WaypointEnemy extends EnemyEntity {

    private int mMaxSpeedX;
    private int mWaypointX; // where this enemy is headed towards
    private long mLastWaypointSetTime;
    private long mWaypointUpdatePeriod; // in milliseconds

    /**
     * @param context
     * @param type
     * @param screenX
     * @param screenY
     * @param endDistance
     * @param maxSpeedX
     * @param waypointUpdatePeriod in milliseconds
     */
    public WaypointEnemy(Context context, Type type, int screenX,
                         int screenY, float endDistance, int maxSpeedX,
                         long waypointUpdatePeriod) {
        super(context, type, screenX, screenY, endDistance);

        mMaxSpeedX = maxSpeedX;
        mWaypointX = 0;
        mLastWaypointSetTime = System.currentTimeMillis();
        mWaypointUpdatePeriod = waypointUpdatePeriod;
    }

    protected int getMaxSpeedX() {
        return mMaxSpeedX;
    }

    protected int getWaypointX() {
        return mWaypointX;
    }

    protected float getLastWaypointSetTime() {
        return mLastWaypointSetTime;
    }

    protected void setLastWaypointSetTime(long newTime) {
        mLastWaypointSetTime = newTime;
    }

    protected float getWaypointUpdatePeriod() {
        return mWaypointUpdatePeriod;
    }

    /**
     * @param newWaypointX
     * @post if enough time has passed, waypoint on x-axis
     * has been updated
     */
    public void setWaypointX(int newWaypointX) {
        if (System.currentTimeMillis() >
                mLastWaypointSetTime + mWaypointUpdatePeriod) {
            mLastWaypointSetTime = System.currentTimeMillis();
            mWaypointX = newWaypointX;
        }
    }

    /**
     * @param playerSpeedY is used to move the enemy further,
     * creating the illusion that the player is moving forward
     * @post enemy has been moved vertically, and has been moved
     * horizontally towards waypoint
     */
    @Override
    public boolean update(int playerSpeedY) {
        // Move enemy towards waypoint
        int centerX = getCenterX();
        if (mWaypointX < centerX)
            setSpeedX(-mMaxSpeedX);
        else if (mWaypointX > centerX)
            setSpeedX(mMaxSpeedX);
        else
            setSpeedX(0);

        return super.update(playerSpeedY);
    }
}
