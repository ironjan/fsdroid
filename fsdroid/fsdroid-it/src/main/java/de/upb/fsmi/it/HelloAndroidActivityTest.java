package de.upb.fsmi.it;

import android.test.ActivityInstrumentationTestCase2;
import de.upb.fsmi.FSDroid;
import de.upb.fsmi.FSDroid_;

public class HelloAndroidActivityTest extends
		ActivityInstrumentationTestCase2<FSDroid_> {

	public HelloAndroidActivityTest() {
		super(FSDroid_.class);
		
	}

	public void testActivity() {
		FSDroid activity = getActivity();
		assertNotNull(activity);
	}
}
