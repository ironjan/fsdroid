package com.github.ironjan.fsupb;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.github.ironjan.fsupb.fragments.NewsFragment_;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.ViewById;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@SuppressLint("InlinedApi")
@EActivity(R.layout.activity_with_drawer)
public class FSDroid extends SherlockFragmentActivity {
	DrawerLayout mDrawerLayout;

	@ViewById
	TextView drawerItemNews, drawerItemContact, drawerItemLicenses;

	TextView drawerItemOPhase, drawerItemMisc, drawerItemCouncil,
			drawerItemMeetings, drawerItemAbout;

	ActionBarDrawerToggle mDrawerToggle;

	String[] title;
	String[] subtitle;

	int[] icon;

	@AfterViews
	void addDrawerShadow() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		switchContentTo(new NewsFragment_());
	}

	@AfterViews
	void addDrawerClickListener() {
		View.OnClickListener onClickListener = new DrawerClickListener(this);
		addCLickListenerTo(drawerItemNews, onClickListener);
		addCLickListenerTo(drawerItemAbout, onClickListener);
		addCLickListenerTo(drawerItemOPhase, onClickListener);
		addCLickListenerTo(drawerItemMisc, onClickListener);
		addCLickListenerTo(drawerItemCouncil, onClickListener);
		addCLickListenerTo(drawerItemMeetings, onClickListener);
		addCLickListenerTo(drawerItemContact, onClickListener);
		addCLickListenerTo(drawerItemAbout, onClickListener);
		addCLickListenerTo(drawerItemLicenses, onClickListener);
	}

	private static void addCLickListenerTo(View v,
			OnClickListener onClickListener) {
		if (v != null) {
			v.setOnClickListener(onClickListener);
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
