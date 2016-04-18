package enterprises.wayne.spacecraftgame;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Aaron on 12/27/2015.
 */
public class MainActivityFunctionalTest
    extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;
    private ImageButton mHero1;
    private ImageButton mHero2;

    public MainActivityFunctionalTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // call before starting the activity;
        // key events won't be ignored
        setActivityInitialTouchMode(false);

        // this is enough to start the activity
        mActivity = getActivity();

        mHero1 = (ImageButton) mActivity.findViewById(R.id.ibHero);
        mHero2 = (ImageButton) mActivity.findViewById(R.id.ibHero2);
    }

    /**
     * Note that this test always fails if the Android device isn't unlocked
     */
    public void testGameActivityLaunch() {
        Instrumentation.ActivityMonitor monitor =
                getInstrumentation().addMonitor(GameActivity.class.getName(),
                        null, false);

        TouchUtils.clickView(this, mHero1);

        GameActivity gameActivity =
                (GameActivity) monitor.waitForActivityWithTimeout(2000);
        assertNotNull(gameActivity);

        getInstrumentation().removeMonitor(monitor);
        sendKeys(KeyEvent.KEYCODE_BACK);
    }
}
