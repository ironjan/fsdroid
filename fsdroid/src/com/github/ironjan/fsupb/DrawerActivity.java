package com.github.ironjan.fsupb;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
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

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.ViewById;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@SuppressLint("InlinedApi")
@EActivity(R.layout.activity_with_drawer)
public class DrawerActivity extends SherlockFragmentActivity {
	private static final String TAG = DrawerActivity.class.getSimpleName();

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

	}

	@AfterViews
	void addDrawerClickListener() {

		View.OnClickListener onClickListener = new DrawerClickListener(this);
		drawerItemNews.setOnClickListener(onClickListener);
		drawerItemOPhase.setOnClickListener(onClickListener);
		drawerItemMisc.setOnClickListener(onClickListener);
		drawerItemCouncil.setOnClickListener(onClickListener);
		drawerItemMeetings.setOnClickListener(onClickListener);
		drawerItemContact.setOnClickListener(onClickListener);
		drawerItemAbout.setOnClickListener(onClickListener);
		drawerItemLicenses.setOnClickListener(onClickListener);
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
		Crouton.showText(DrawerActivity.this, text, Style.INFO);
	}

	void switchContentTo(Fragment fragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.content_frame, fragment);
		ft.commit();
		mDrawerLayout.closeDrawer(Gravity.LEFT);
	}

	@Override
	protected void onDestroy() {
		Crouton.cancelAllCroutons();
		super.onDestroy();
	}
}
