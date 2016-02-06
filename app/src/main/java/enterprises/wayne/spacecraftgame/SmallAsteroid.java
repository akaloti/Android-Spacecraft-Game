package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 1/17/2016.
 */
public class SmallAsteroid extends EnemyEntity {

    public SmallAsteroid(Context context, int screenX, int screenY,
                         float endDistance) {
        super(context, Type.SMALL_ASTEROID, screenX, screenY, endDistance);
    }
}
