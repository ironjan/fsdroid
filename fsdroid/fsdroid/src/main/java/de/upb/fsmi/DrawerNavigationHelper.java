package de.upb.fsmi;

import android.view.*;
import de.upb.fsmi.fragments.*;

final class DrawerNavigationHelper {

	private static LibrariesFragment_ librariesFragment;
	private static ContactFragment_ contactFragment;
	private static CouncilFragment_ councilFragment;
	private static OPhaseFragment_ oPhaseFragment;
	private static NewsFragment_ newsFragment;

	public static void navigate(FSDroid activity, View clickedView) {
		TestFragment tf = new TestFragment();
		switch (clickedView.getId()) {
		case R.id.drawerItemNews:
			activity.switchContentTo(getNewsFragment());
			break;
		case R.id.drawerItemOPhase:
			activity.switchContentTo(getOPhaseFragment());
			break;
		case R.id.drawerItemMisc:
			tf.setText("misc");
			activity.switchContentTo(tf);
			break;
		case R.id.drawerItemCouncil:
			activity.switchContentTo(getCouncilFragment());
			break;
		case R.id.drawerItemMeetings:
			tf.setText("meetings");
			activity.switchContentTo(tf);
			break;
		case R.id.drawerItemContact:
			activity.switchContentTo(getContactFragment());
			break;
		case R.id.drawerItemAbout:
			tf.setText("about");
			activity.switchContentTo(tf);
			break;
		case R.id.drawerItemLicenses:
			tf.setText("licenses");
			activity.switchContentTo(getLibraryFragment());
			break;
		default:
			tf.setText("default?!");
			activity.switchContentTo(tf);
			break;
		}
	}

	private static LibrariesFragment_ getLibraryFragment() {
		if (librariesFragment == null) {
			librariesFragment = new LibrariesFragment_();
		}
		return librariesFragment;
	}

	private static ContactFragment_ getContactFragment() {
		if (contactFragment == null) {
			contactFragment = new ContactFragment_();
		}
		return contactFragment;
	}

	private static CouncilFragment_ getCouncilFragment() {
		if (councilFragment == null) {
			councilFragment = new CouncilFragment_();
		}
		return councilFragment;
	}

	private static OPhaseFragment_ getOPhaseFragment() {
		if (oPhaseFragment == null) {
			oPhaseFragment = new OPhaseFragment_();
		}
		return oPhaseFragment;
	}

	private static NewsFragment_ getNewsFragment() {
		if (newsFragment == null) {
			newsFragment = new NewsFragment_();
		}
		return newsFragment;
	}

}