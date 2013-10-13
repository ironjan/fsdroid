package de.upb.fsmi;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.ViewById;

import de.upb.fsmi.fragments.NewsFragment_;

@EActivity(R.layout.activity_with_drawer)
public class FSDroid extends ActionBarActivity {
	static final String TAG = FSDroid.class.getSimpleName();

	DrawerLayout mDrawerLayout;

	@ViewById
	TextView drawerItemNews, drawerItemOPhase, drawerItemMisc,
			drawerItemCouncil, drawerItemMeetings, drawerItemContact,
			drawerItemAbout, drawerItemLicenses;

	ActionBarDrawerToggle mDrawerToggle;

	private Tab[] tabs = null;
	private int selectedTab = 0;

	private Fragment displayedFragment = null;

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_PROGRESS);
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	}

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
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mDrawerToggle = new DrawerOpenCloseListener(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Click({ R.id.drawerItemNews, R.id.drawerItemOPhase, R.id.drawerItemMisc,
			R.id.drawerItemCouncil, R.id.drawerItemMeetings,
			R.id.drawerItemContact, R.id.drawerItemAbout,
			R.id.drawerItemLicenses, R.id.drawerItemMap })
	void navigationDrawerElementsClicked(View v) {
		DrawerNavigationHelper.navigate(this, v);
	}

	@SuppressLint("InlinedApi")
	@OptionsItem(android.R.id.home)
	void toggleDrawer() {
		if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			closeDrawer();
		} else {
			openDrawer();
		}
	}

	private void closeDrawer() {
		mDrawerLayout.closeDrawer(Gravity.LEFT);
		restoreTabs();
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
		tabs = null;
	}

	private void openDrawer() {
		if (ActionBar.NAVIGATION_MODE_TABS == getSupportActionBar()
				.getNavigationMode()) {
			saveTabs();
		}
		mDrawerLayout.openDrawer(Gravity.LEFT);
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
		if (displayedFragment == fragment) {
			return;
		}
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.addToBackStack(null);
		ft.replace(R.id.content_frame, fragment);
		getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);
		ft.commit();
		closeDrawer();
	}
}
