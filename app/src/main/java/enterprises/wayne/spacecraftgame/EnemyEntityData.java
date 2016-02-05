package enterprises.wayne.spacecraftgame;

/**
 * Created by Aaron on 1/19/2016.
 */
public class EnemyEntityData {
    public Entity.Type type;
    public boolean hasSpawned;
    public float startDistance;
    public float endDistance;

    public EnemyEntityData(Entity.Type type,
                           float startDistance, float endDistance) {
        this.type = type;
        this.hasSpawned = false;
        this.startDistance = startDistance;
        this.endDistance = endDistance;
    }
}
