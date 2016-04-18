package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 4/8/2016.
 */
public class FastDummy extends EnemyEntity {
    public FastDummy(Context context, int screenX, int screenY, float endDistance) {
        super(context, Type.FAST_DUMMY_1, screenX, screenY, endDistance);
    }
}
