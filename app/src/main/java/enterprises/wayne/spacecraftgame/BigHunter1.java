package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 3/28/2016.
 */
public class BigHunter1 extends WaypointEnemy {

    public BigHunter1(Context context, int screenX, int screenY, float endDistance) {
        super(context, Type.BIG_HUNTER_1, screenX, screenY, endDistance, 2, 100);
    }
}
