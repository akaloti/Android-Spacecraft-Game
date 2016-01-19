package enterprises.wayne.spacecraftgame;

/**
 * Created by Aaron on 1/19/2016.
 */
public class EnemyEntityData {
    public Entity.Type mType;
    public float mStartDistance;
    public float mEndDistance;

    public EnemyEntityData(Entity.Type type, float startDistance,
                           float endDistance) {
        mType = type;
        mStartDistance = startDistance;
        mEndDistance = endDistance;
    }
}
