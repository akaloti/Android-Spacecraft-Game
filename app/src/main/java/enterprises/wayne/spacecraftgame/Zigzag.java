package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 3/22/2016.
 */
public class Zigzag extends WaypointEnemy {

    public Zigzag(Context context, int screenX, int screenY, float endDistance) {
        super(context, Type.ZIGZAG_1, screenX, screenY, endDistance, 1, 1000);
    }

    @Override
    public boolean update(int playerSpeedY) {
        setWaypointX(getMaxX()); // <-- CHANGE

        // Do appropriate movement regarding waypoint and player's speed
        return super.update(playerSpeedY);
    }
}
