package com.github.ironjan.fsupb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.github.ironjan.fsupb.fragments.TestFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.ViewById;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@SuppressLint("InlinedApi")
@EActivity(R.layout.activity_with_drawer)
public class FSDroid extends SherlockFragmentActivity {
	private static final String TAG = FSDroid.class.getSimpleName();

	DrawerLayout mDrawerLayout;

	@ViewById
	TextView drawerItemNews, drawerItemOPhase, drawerItemMisc,
			drawerItemCouncil, drawerItemMeetings, drawerItemContact,
			drawerItemAbout, drawerItemLicenses;

	ActionBarDrawerToggle mDrawerToggle;
	String[] title;
	String[] subtitle;
	int[] icon;

	@AfterViews
	void addDrawerShadow() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				Log.v(TAG, "drawer closed");
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) {
				Log.v(TAG, "Drawer opened");
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		switchContentTo(new NewsFragment_());
	}

	@Click({ R.id.drawerItemNews, R.id.drawerItemOPhase, R.id.drawerItemMisc,
			R.id.drawerItemCouncil, R.id.drawerItemMeetings,
			R.id.drawerItemContact, R.id.drawerItemAbout,
			R.id.drawerItemLicenses })
	void navigationDrawerElementsClicked(View v) {
		TestFragment tf = new TestFragment();
		switch (v.getId()) {
		case R.id.drawerItemNews:
			switchContentTo(new NewsFragment_());
			break;
		case R.id.drawerItemOPhase:
			switchContentTo(new OPhaseFragment_());
			break;
		case R.id.drawerItemMisc:
			tf.setText("misc");
			switchContentTo(tf);
			break;
		case R.id.drawerItemCouncil:
			switchContentTo(new CouncilFragment_());
			break;
		case R.id.drawerItemMeetings:
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("geo:51.70692,8.771176?z=20"));
			startActivity(intent);
			break;
		case R.id.drawerItemContact:
			Intent intent2 = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://maps.google.de/maps?q=51.70837,8.771176&hl=de&ll=51.706959,8.771145&spn=0.000826,0.00203&num=1&t=h&z=19"));
			startActivity(intent2);
			break;
		case R.id.drawerItemAbout:
			tf.setText("about");
			switchContentTo(tf);
			break;
		case R.id.drawerItemLicenses:
			tf.setText("licenses");
			switchContentTo(tf);
			break;
		default:
			tf.setText("default?!");
			switchContentTo(tf);
			break;
		}
	}

	@OptionsItem(android.R.id.home)
	void toggleDrawer() {

		if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		} else {
			mDrawerLayout.openDrawer(Gravity.LEFT);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.syncState();
	}

	void notifyUser(String text) {
		Crouton.showText(FSDroid.this, text, Style.INFO);
	}

	void switchContentTo(Fragment fragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.content_frame, fragment);
		getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);
		ft.commit();
		mDrawerLayout.closeDrawer(Gravity.LEFT);
	}

	@Override
	protected void onDestroy() {
		Crouton.cancelAllCroutons();
		super.onDestroy();
	}

	public void closeDrawer() {
		mDrawerLayout.closeDrawer(Gravity.LEFT);
	}
}
