package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 4/9/2016.
 */
public class SuperFastDummy extends EnemyEntity {
    public SuperFastDummy(Context context, int screenX, int screenY, float endDistance) {
        super(context, Entity.Type.SUPER_FAST_DUMMY_1, screenX, screenY, endDistance);
    }
}
