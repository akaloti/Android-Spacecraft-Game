package enterprises.wayne.spacecraftgame;

import java.util.ArrayList;

/**
 * Created by Aaron on 2/7/2016.
 */
public class LevelData {
    public ArrayList<EnemyEntity> enemyEntities;
    public float totalDistance;
    public int backgroundMusicResId;

    public LevelData(ArrayList<EnemyEntity> enemyEntities,
                     float totalDistance, int backgroundMusicResId) {
        this.enemyEntities = enemyEntities;
        this.totalDistance = totalDistance;
        this.backgroundMusicResId = backgroundMusicResId;
    }
}
