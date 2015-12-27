package enterprises.wayne.spacecraftgame;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;

/**
 * Created by Aaron on 12/27/2015.
 */
public class MainActivityFunctionalTest
    extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;
    private Button mPlay;

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

        mPlay = (Button) mActivity.findViewById(R.id.bPlay);
    }

    /**
     * Note that this test always fails if the Android device isn't unlocked
     */
    public void testGameActivityLaunch() {
        Instrumentation.ActivityMonitor monitor =
                getInstrumentation().addMonitor(GameActivity.class.getName(),
                        null, false);

        TouchUtils.clickView(this, mPlay);

        GameActivity gameActivity =
                (GameActivity) monitor.waitForActivityWithTimeout(2000);
        assertNotNull(gameActivity);

        getInstrumentation().removeMonitor(monitor);
        sendKeys(KeyEvent.KEYCODE_BACK);
    }
}
