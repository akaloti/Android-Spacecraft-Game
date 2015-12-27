package enterprises.wayne.spacecraftgame;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

/**
 * Created by Aaron on 12/27/2015.
 */
public class GameActivity extends Activity {

    private SGView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();
        // Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        mGameView = new SGView(this, size.x, size.y);
        setContentView(mGameView);
    }
}
