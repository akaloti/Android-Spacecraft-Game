package enterprises.wayne.spacecraftgame;

import android.content.Context;

/**
 * Created by Aaron on 12/28/2015.
 */
public class PlayerSpacecraft extends Spacecraft {

    /**
     * @param context to allow access to drawables
     * @param type so that appropriate bitmap can be selected
     * @param screenX user's screen's width (in pixels)
     * @param screenY user's screen's height (in pixels)
     */
    public PlayerSpacecraft(Context context, Type type,
                            int screenX, int screenY) {
        super(context, type, screenX, screenY);
    }

    public void update() {

    }
}
