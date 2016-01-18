package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 12/31/2015.
 */
public class Dummy extends EnemyEntity {

    public Dummy(Context context, int screenX, int screenY) {
        super(context, Type.DUMMY_1, screenX, screenY);
    }
}
