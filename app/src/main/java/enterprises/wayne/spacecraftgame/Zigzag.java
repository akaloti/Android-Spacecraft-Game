package enterprises.wayne.spacecraftgame;

import android.content.Context;

import java.util.Random;

/**
 * Created by Aaron on 3/22/2016.
 */
public class Zigzag extends WaypointEnemy {

    private int mWaypoint1;
    private int mWaypoint2;
    private int mCurrWaypoint;
    private final int mDistanceBetweenWaypoints;

    public Zigzag(Context context, int screenX, int screenY, float endDistance) {
        super(context, Type.ZIGZAG_1, screenX, screenY, endDistance, 4, 700);

        mDistanceBetweenWaypoints = screenX / 3;

        // Have to call again, now that have correct distance between waypoints
        decideWaypoints();
    }

    private void decideWaypoints() {
        // decide two waypoints in a way so that the spacecraft doesn't glide along
        // either side boundary ever
        mWaypoint1 = Math.max(getCenterX() - mDistanceBetweenWaypoints / 2, getMinX());
        mWaypoint2 = Math.min(mWaypoint1 + mDistanceBetweenWaypoints, getMaxX());

        // Don't always have it start going the same direction
        Random generator = new Random();
        mCurrWaypoint = generator.nextInt(2) + 1;
        setWaypointX((mCurrWaypoint == 1) ? mWaypoint1 : mWaypoint2);
    }

    @Override
    protected void respawn() {
        // Do this first because position needs to be changed
        super.respawn();

        decideWaypoints();
    }

    @Override
    public boolean update(int playerSpeedY) {
        // Expect setWaypointX() to check if can switch the waypoint
        boolean changedWaypoint =
                setWaypointX((mCurrWaypoint == 1) ? mWaypoint2 : mWaypoint1);
        if (changedWaypoint)
            mCurrWaypoint = (mCurrWaypoint == 1) ? 2 : 1;

        // Do appropriate movement regarding waypoint and player's speed
        return super.update(playerSpeedY);
    }
}
