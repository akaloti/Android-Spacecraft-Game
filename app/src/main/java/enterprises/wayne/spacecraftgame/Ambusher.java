package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 3/30/2016.
 */
public class Ambusher extends WaypointEnemy {

    private static final float OFFSET_MULTIPLIER = 10;

    public Ambusher(Context context, int screenX, int screenY,
                    float endDistance) {
        super(context, Type.AMBUSHER_1, screenX, screenY, endDistance, 4, 300);
    }

    /**
     * @param playerCenterX
     * @param playerSpeedX
     * @return where the enemy "thinks" the player is headed,
     * based on the player's position
     */
    public int determineOffset(int playerCenterX, int playerSpeedX) {
        // THINK OF A BETTER WAY TO DETERMINE THE RETURN VALUE
        return 0;
        /*if (playerSpeedX != 0)
            return (int) (playerCenterX + playerSpeedX * OFFSET_MULTIPLIER);
        else // don't move
            return playerCenterX;*/
    } // determineOffset()
}
