package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 1/1/2016.
 */
public class Hunter extends WaypointEnemy {

    public Hunter(Context context, int screenX, int screenY, float endDistance) {
        super(context, Type.HUNTER_1, screenX, screenY, endDistance, 2, 500);
    }
}
