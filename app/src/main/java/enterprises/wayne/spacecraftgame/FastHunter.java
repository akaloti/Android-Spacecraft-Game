package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 4/8/2016.
 */
public class FastHunter extends WaypointEnemy {
    public FastHunter(Context context, int screenX, int screenY, float endDistance) {
        super(context, Type.FAST_HUNTER_1, screenX, screenY, endDistance, 3, 400);
    }
}
