package enterprises.wayne.spacecraftgame;

import android.content.Context;

import java.util.Random;

/**
 * Created by Aaron on 12/31/2015.
 */
public abstract class EnemySpacecraft extends Entity {

    private int mMaxY, mMinY; // for repositioning

    /**
     * @param context to allow access to drawables
     * @param type so that appropriate bitmap can be selected
     * @param screenX user's screen's width (in pixels)
     * @param screenY user's screen's height (in pixels)
     */
    public EnemySpacecraft(Context context, Type type,
                           int screenX, int screenY) {
        super(context, type, screenX, screenY);

        // enemy spacecrafts can be off screen briefly (to create
        // the illusion that the spacecrafts are not somehow warping)
        mMaxY = screenY + getBitmap().getHeight();
        mMinY = -1 * getBitmap().getHeight();

        // respawn after minimum and maximum positions have been set
        respawn();
    }

    public int getMaxY() {
        return mMaxY;
    }

    public int getMinY() {
        return mMinY;
    }

    /**
     * @param playerSpeedY is used to move the enemy further,
     * creating the illusion that the player is moving forward
     * (so that the camera needn't move)
     */
    public void update(int playerSpeedY) {
        setX(getX() + getSpeedX());
        setY(getY() + playerSpeedY + getSpeedY());

        // respawn when off screen
        if (getY() > mMaxY)
            respawn();

        updateHitBox();
    }

    /**
     * @post spacecraft's new speed and position have been chosen
     */
    protected void respawn() {
        setRandomNewPosition();
        setRandomNewSpeedY();
    }

    /**
     * @post this spacecraft's y-speed has been randomly set
     */
    private void setRandomNewSpeedY() {
        Random generator = new Random();
        setSpeedY(generator.nextInt(3) + 5);
    }

    /**
     * @post this spacecraft's position has been set to a horizontally
     * random one that is just above the screen's viewable area
     */
    private void setRandomNewPosition() {
        // Set the spacecraft just above the user's view
        int y = mMinY;

        // Pick a random, valid horizontal coordinate
        Random generator = new Random();
        int x = generator.nextInt(getMaxX());

        setX(x);
        setY(y);
    }
}
