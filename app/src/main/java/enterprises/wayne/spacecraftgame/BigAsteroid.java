package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 3/28/2016.
 */
public class BigAsteroid extends EnemyEntity {

    public BigAsteroid(Context context, int screenX, int screenY,
                         float endDistance) {
        super(context, Type.BIG_ASTEROID, screenX, screenY, endDistance);
    }
}
