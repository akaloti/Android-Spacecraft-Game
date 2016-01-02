package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 1/1/2016.
 */
public class Hunter extends EnemySpacecraft {

    // For hunting the player; should be slower than player on x-axis
    private int mHorizontalWaypoint;
    private static final int HORIZONTAL_SPEED = 5;

    public Hunter(Context context, int screenX, int screenY) {
        super(context, Type.HUNTER_1, screenX, screenY);

        mHorizontalWaypoint = 0;
    }

    /**
     * @param playerSpeedY is used to move the enemy further,
     * creating the illusion that the player is moving forward
     * @post enemy has been moved vertically, and has been moved
     * horizontally towards waypoint
     */
    @Override
    public void update(int playerSpeedY) {
        super.update(playerSpeedY);

        // Move enemy towards waypoint

    }
}
