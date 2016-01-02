package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 12/31/2015.
 */
public class EnemySpacecraft1 extends EnemySpacecraft {

    public EnemySpacecraft1(Context context, int screenX, int screenY) {
        super(context, Type.ENEMY_1, screenX, screenY);

        setSpeedY(4);
        setRandomPosition();
    }
}
