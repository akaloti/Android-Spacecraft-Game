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
    private ImageButton[] mImageButtons = new ImageButton[3];

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

        mImageButtons[0] = (ImageButton) mActivity.findViewById(R.id.ibHero);
        mImageButtons[1] = (ImageButton) mActivity.findViewById(R.id.ibHero2);
        mImageButtons[2] = (ImageButton) mActivity.findViewById(R.id.ibHero3);
    }

    public void testHeroSelection() {
        int i = 1;
        for (ImageButton ib : mImageButtons) {
            ib.performClick();

            Intent triggeredIntent = getStartedActivityIntent();
            assertNotNull("Intent was null in iteration " + i, triggeredIntent);

            // Check if the button's corresponding hero is recorded
            assertNotNull("Chosen spacecraft not recorded in iteration " + i,
                    MainActivity.getChosenSpacecraft());
            assertEquals("Wrong res id recorded in iteration " + i,
                    MainActivity.resIdToEntityType(ib.getId()),
                    MainActivity.getChosenSpacecraft());

            ++i;
        } // for each image button
    }
}
