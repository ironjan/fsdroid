package de.upb.fsmi;

import android.view.View;
import de.upb.fsmi.fragments.ContactFragment_;
import de.upb.fsmi.fragments.CouncilFragment_;
import de.upb.fsmi.fragments.LibrariesFragment_;
import de.upb.fsmi.fragments.NewsFragment_;
import de.upb.fsmi.fragments.OPhaseFragment_;
import de.upb.fsmi.fragments.TestFragment;

final class DrawerClickListener implements View.OnClickListener {
	/**
	 * 
	 */
	private final FSDroid drawerActivity;

	/**
	 * @param drawerActivity
	 */
	public DrawerClickListener(FSDroid drawerActivity) {
		this.drawerActivity = drawerActivity;
	}

	@Override
	public void onClick(View v) {
		TestFragment tf = new TestFragment();
		switch (v.getId()) {
		case R.id.drawerItemNews:
			drawerActivity.switchContentTo(new NewsFragment_());
			break;
		case R.id.drawerItemOPhase:
			drawerActivity.switchContentTo(new OPhaseFragment_());
			break;
		case R.id.drawerItemMisc:
			tf.setText("misc");
			drawerActivity.switchContentTo(tf);
			break;
		case R.id.drawerItemCouncil:
			drawerActivity.switchContentTo(new CouncilFragment_());
			break;
		case R.id.drawerItemMeetings:
			tf.setText("meetings");
			break;
		case R.id.drawerItemContact:
			drawerActivity.switchContentTo(new ContactFragment_());
			break;
		case R.id.drawerItemAbout:
			tf.setText("about");
			drawerActivity.switchContentTo(tf);
			break;
		case R.id.drawerItemLicenses:
			drawerActivity.switchContentTo(new LibrariesFragment_());
			break;
		default:
			tf.setText("default?!");
			drawerActivity.switchContentTo(tf);
			break;
		}
	}
}