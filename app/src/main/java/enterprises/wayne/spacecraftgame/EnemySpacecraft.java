package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 12/31/2015.
 */
public abstract class EnemySpacecraft extends Spacecraft {

    /**
     * @param context to allow access to drawables
     * @param type so that appropriate bitmap can be selected
     * @param screenX user's screen's width (in pixels)
     * @param screenY user's screen's height (in pixels)
     */
    public EnemySpacecraft(Context context, Type type,
                           int screenX, int screenY) {
        super(context, type, screenX, screenY);


    }

    /**
     * @param playerSpeedY is used to move the enemy further,
     * creating the illusion that the player is moving forward
     * (so that the camera needn't move)
     */
    public void update(int playerSpeedY) {
        setY(getY() - playerSpeedY - getSpeedY());
    }
}
