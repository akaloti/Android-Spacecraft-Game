package enterprises.wayne.spacecraftgame;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Aaron on 2/7/2016.
 */
public class LevelData {
    public CopyOnWriteArrayList<EnemyEntityData> enemyData;
    public float goalDistance;
    public int backgroundMusicResId;

    // Default constructor
    public LevelData() {
        this.enemyData = null;
        this.goalDistance = 0;
        this.backgroundMusicResId = -1; // should cause bug if not changed
    }

    public LevelData(CopyOnWriteArrayList<EnemyEntityData> enemyData,
                     float goalDistance, int backgroundMusicResId) {
        this.enemyData = enemyData;
        this.goalDistance = goalDistance;
        this.backgroundMusicResId = backgroundMusicResId;
    }
}
