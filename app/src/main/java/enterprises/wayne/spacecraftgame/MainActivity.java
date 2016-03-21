package enterprises.wayne.spacecraftgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    
    // This is probably bad in that it couples MainActivity to SGView,
    // but the alternatives (e.g. use of serialized types) seem worse,
    // and this project is small, anyway
    private static Entity.Type whichSpacecraft = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
    }

    public static Entity.Type getChosenSpacecraft() {
        return whichSpacecraft;
    }

    public void onPlayClick(View view) {
        whichSpacecraft = resIdToEntityType(view.getId());
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    public static Entity.Type resIdToEntityType(int spacecraftResId) {
        if (spacecraftResId == R.id.ibHero)
            return Entity.Type.HERO_1;
        else if (spacecraftResId == R.id.ibHero2)
            return Entity.Type.HERO_2;
        else if (spacecraftResId == R.id.ibHero3)
            return Entity.Type.HERO_3;
        else
            throw new AssertionError(
                    "Didn't find entity corresponding to res id");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // If the player hits the back button, quit the app
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }
}
