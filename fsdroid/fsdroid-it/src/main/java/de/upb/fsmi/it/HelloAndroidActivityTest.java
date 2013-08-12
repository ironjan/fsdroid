package de.upb.fsmi.it;

import android.test.ActivityInstrumentationTestCase2;
import de.upb.fsmi.HelloAndroidActivity;

public class HelloAndroidActivityTest extends
		ActivityInstrumentationTestCase2<HelloAndroidActivity> {

	public HelloAndroidActivityTest() {
		super(HelloAndroidActivity.class);
	}

	public void testActivity() {
		HelloAndroidActivity activity = getActivity();
		assertNotNull(activity);
	}
}
