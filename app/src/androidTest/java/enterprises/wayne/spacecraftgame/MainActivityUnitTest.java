package enterprises.wayne.spacecraftgame;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.Button;

/**
 * Created by Aaron on 12/27/2015.
 */
public class MainActivityUnitTest
    extends ActivityUnitTestCase<MainActivity> {

    private MainActivity mActivity;
    private Button mPlay;

    public MainActivityUnitTest() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();

        // In unit tests, the activity isn't automatically
        // started by the system, so we must start it
        Intent intent = new
                Intent(getInstrumentation().getTargetContext(),
                MainActivity.class);
        startActivity(intent, null, null);
        mActivity = getActivity();

        mPlay = (Button) mActivity.findViewById(R.id.bPlay);
    }

    public void testGameActivityLaunch() {
        mPlay.performClick();

        Intent triggeredIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", triggeredIntent);
    }
}
