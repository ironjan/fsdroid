package de.upb.fsmi.it;

import android.test.ActivityInstrumentationTestCase2;
import de.upb.fsmi.*;

public class HelloAndroidActivityTest extends
		ActivityInstrumentationTestCase2<FSDroid_> {

	public HelloAndroidActivityTest() {
		super(FSDroid_.class);
		
	}

	public void testActivity() {
		DrawerActivity activity = getActivity();
		assertNotNull(activity);
	}
}
