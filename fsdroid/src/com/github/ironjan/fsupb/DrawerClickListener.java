package com.github.ironjan.fsupb;

import com.github.ironjan.fsupb.fragments.NewsFragment_;
import com.github.ironjan.fsupb.fragments.TestFragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

final class DrawerClickListener implements View.OnClickListener {
	/**
	 * 
	 */
	private final DrawerActivity drawerActivity;

	/**
	 * @param drawerActivity
	 */
	public DrawerClickListener(DrawerActivity drawerActivity) {
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
			tf.setText("ophase");
			drawerActivity.switchContentTo(tf);
			break;
		case R.id.drawerItemMisc:
			tf.setText("misc");
			drawerActivity.switchContentTo(tf);
			break;
		case R.id.drawerItemCouncil:
			tf.setText("council");
			drawerActivity.switchContentTo(tf);
			break;
		case R.id.drawerItemMeetings:

			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("geo:51.70692,8.771176?z=20"));
			drawerActivity.startActivity(intent);
			break;
		case R.id.drawerItemContact:
			Intent intent2 = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://maps.google.de/maps?q=51.70837,8.771176&hl=de&ll=51.706959,8.771145&spn=0.000826,0.00203&num=1&t=h&z=19"));
			drawerActivity.startActivity(intent2);
			break;
		case R.id.drawerItemAbout:
			tf.setText("about");
			drawerActivity.switchContentTo(tf);
			break;
		case R.id.drawerItemLicenses:
			tf.setText("licenses");
			drawerActivity.switchContentTo(tf);
			break;
		default:
			tf.setText("default?!");
			drawerActivity.switchContentTo(tf);
			break;
		}
	}
}