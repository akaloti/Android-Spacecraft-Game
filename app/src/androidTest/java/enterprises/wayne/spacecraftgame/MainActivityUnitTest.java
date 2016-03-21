package enterprises.wayne.spacecraftgame;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.ImageButton;

/**
 * Created by Aaron on 12/27/2015.
 */
public class MainActivityUnitTest
    extends ActivityUnitTestCase<MainActivity> {

    private MainActivity mActivity;
    private ImageButton mHero1;
    private ImageButton mHero2;

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

        mHero1 = (ImageButton) mActivity.findViewById(R.id.ibHero);
        mHero2 = (ImageButton) mActivity.findViewById(R.id.ibAsteroid);
    }

    public void testHeroSelection() {
        mHero1.performClick();

        Intent triggeredIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", triggeredIntent);

        // Check if the button's corresponding hero is recorded
        assertNotNull(MainActivity.getChosenSpacecraft());
        assertEquals(MainActivity.resIdToEntityType(mHero1.getId()),
                MainActivity.getChosenSpacecraft());

        mHero2.performClick();

        triggeredIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", triggeredIntent);
    }
}
