package de.upb.fsmi;

import android.support.v4.app.Fragment;
import android.view.View;
import de.upb.fsmi.fragments.ContactFragment_;
import de.upb.fsmi.fragments.CouncilFragment_;
import de.upb.fsmi.fragments.LibrariesFragment_;
import de.upb.fsmi.fragments.MapFragment_;
import de.upb.fsmi.fragments.NewsFragment_;
import de.upb.fsmi.fragments.TestFragment;

final class DrawerNavigationHelper {

	private static LibrariesFragment_ librariesFragment;
	private static ContactFragment_ contactFragment;
	private static CouncilFragment_ councilFragment;
	private static NewsFragment_ newsFragment;
	private static MapFragment_ mapFragment;

	public static void navigate(FSDroid activity, View clickedView) {
		TestFragment tf = new TestFragment();
		switch (clickedView.getId()) {
		case R.id.drawerItemNews:
			activity.switchContentTo(getNewsFragment());
			break;
		case R.id.drawerItemOPhase:
			tf.setText("ophase");
			activity.switchContentTo(tf);
						break;
		case R.id.drawerItemMap:
			activity.switchContentTo(getMapFragment());
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
			activity.switchContentTo(getLibraryFragment());
			break;
		default:
			tf.setText("default?!");
			activity.switchContentTo(tf);
			break;
		}
	}

	private static Fragment getMapFragment() {
		if (mapFragment == null) {
			mapFragment = new MapFragment_();
		}
		return mapFragment;
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

	private static NewsFragment_ getNewsFragment() {
		if (newsFragment == null) {
			newsFragment = new NewsFragment_();
		}
		return newsFragment;
	}

}