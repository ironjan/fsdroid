package de.upb.fsmi;

import android.annotation.*;
import android.content.res.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.app.ActionBar.Tab;
import android.view.*;
import android.widget.*;

import com.googlecode.androidannotations.annotations.*;

import de.upb.fsmi.fragments.*;

@EActivity(R.layout.activity_with_drawer)
public class FSDroid extends ActionBarActivity {
	static final String TAG = FSDroid.class.getSimpleName();

	DrawerLayout mDrawerLayout;

	@ViewById
	TextView drawerItemNews, drawerItemOPhase, drawerItemMisc, drawerItemCouncil,
			drawerItemMeetings, drawerItemContact, drawerItemAbout, drawerItemLicenses;

	ActionBarDrawerToggle mDrawerToggle;

	private Tab[] tabs = null;
	private int selectedTab = 0;

	@AfterViews
	void addDrawerShadow() {
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initDrawer();

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.content_frame, new NewsFragment_());
		ft.commit();
	}

	private void initDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		mDrawerToggle = new DrawerOpenCloseListener(this, mDrawerLayout, R.drawable.ic_drawer,
				R.string.drawer_open, R.string.drawer_close);

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Click({ R.id.drawerItemNews, R.id.drawerItemOPhase, R.id.drawerItemMisc,
			R.id.drawerItemCouncil, R.id.drawerItemMeetings, R.id.drawerItemContact,
			R.id.drawerItemAbout, R.id.drawerItemLicenses })
	void navigationDrawerElementsClicked(View v) {
		DrawerNavigationHelper.navigate(this, v);
	}

	@SuppressLint("InlinedApi")
	@OptionsItem(android.R.id.home)
	void toggleDrawer() {
		if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			mDrawerLayout.closeDrawer(Gravity.LEFT);
			restoreTabs();
		} else {
			if (ActionBar.NAVIGATION_MODE_TABS == getSupportActionBar().getNavigationMode()) {
				saveTabs();
			}
			mDrawerLayout.openDrawer(Gravity.LEFT);
		}
	}

	private void restoreTabs() {
		if (tabs == null || tabs.length == 0) {
			return;
		}

		ActionBar ab = getSupportActionBar();
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (int i = 0; i < tabs.length; i++) {
			ab.addTab(tabs[i]);
		}
		if (selectedTab > 0 && selectedTab < tabs.length) {
			ab.setSelectedNavigationItem(selectedTab);
		}
		selectedTab = 0;
		tabs = null;
	}

	private void saveTabs() {
		ActionBar ab = getSupportActionBar();
		selectedTab = ab.getSelectedTab().getPosition();
		final int count = ab.getTabCount();
		tabs = new Tab[count];
		for (int i = 0; i < count; i++) {
			tabs[i] = ab.getTabAt(i);
		}
		ab.removeAllTabs();
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
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
		Toast.makeText(FSDroid.this, text, Toast.LENGTH_LONG).show();
	}

	void switchContentTo(Fragment fragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.addToBackStack(null);
		ft.replace(R.id.content_frame, fragment);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		ft.commit();
		closeDrawer();
	}

	public void closeDrawer() {
		mDrawerLayout.closeDrawer(Gravity.LEFT);
	}
}
