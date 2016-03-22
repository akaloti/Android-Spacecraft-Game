package enterprises.wayne.spacecraftgame;

import android.content.Context;

import java.util.Random;

/**
 * Created by Aaron on 12/31/2015.
 */
public abstract class EnemyEntity extends Entity {

    private int mMaxY, mMinY; // for repositioning
    private float mEndDistance; // for knowing when to destroy it
    private boolean mIsMarkedForRemoval;

    /**
     * @param context to allow access to drawables
     * @param type so that appropriate bitmap can be selected
     * @param screenX user's screen's width (in pixels)
     * @param screenY user's screen's height (in pixels)
     * @param endDistance
     */
    public EnemyEntity(Context context, Type type,
                       int screenX, int screenY,
                       float endDistance) {
        super(context, type, screenX, screenY);

        // enemy entities can be off screen briefly (to create
        // the illusion that the entities are not somehow warping)
        mMaxY = screenY + getBitmap().getHeight();
        mMinY = -1 * getBitmap().getHeight();

        mEndDistance = endDistance;
        mIsMarkedForRemoval = false;

        // respawn after minimum and maximum positions have been set
        respawn();

        // so the hit box doesn't start at wrong spot and kill player
        updateHitBox();
    }

    public int getMaxY() {
        return mMaxY;
    }

    public int getMinY() {
        return mMinY;
    }

    public float getEndDistance() {
        return mEndDistance;
    }

    public boolean isMarkedForRemoval() {
        return mIsMarkedForRemoval;
    }

    public void markForRemoval() {
        mIsMarkedForRemoval = true;
    }

    /**
     * @param playerSpeedY is used to move the enemy further,
     * creating the illusion that the player is moving forward
     * (so that the camera needn't move)
     * @return false if enemy should be destroyed; otherwise, true
     */
    public boolean update(int playerSpeedY) {
        setX(getX() + getSpeedX());
        setY(getY() + playerSpeedY + getSpeedY());

        if (getY() > mMaxY) {
            // enemy is offscreen; either destroy or respawn
            if (mIsMarkedForRemoval)
                return false;
            else
                respawn();
        }

        updateHitBox();

        return true;
    }

    /**
     * @post entity's new speed and position have been chosen
     */
    protected void respawn() {
        setRandomNewPosition();
        setRandomNewSpeedY();
    }

    /**
     * @post this entity's y-speed has been randomly set
     */
    private void setRandomNewSpeedY() {
        Random generator = new Random();

        // asteroids are slower than spacecrafts are;
        // since there are not many enemy entity types yet,
        // this method of assigning different speed ranges suffices;
        if (getType() == Type.SMALL_ASTEROID)
            setSpeedY(generator.nextInt(2) + 3);
        else
            setSpeedY(generator.nextInt(3) + 5);
    }

    /**
     * @post this entity's position has been set to a horizontally
     * random one that is just above the screen's viewable area
     */
    private void setRandomNewPosition() {
        // Set the entity just above the user's view
        int y = mMinY;

        // Pick a random, valid horizontal coordinate
        Random generator = new Random();
        int x = generator.nextInt(getMaxX());

        setX(x);
        setY(y);
    }
}
