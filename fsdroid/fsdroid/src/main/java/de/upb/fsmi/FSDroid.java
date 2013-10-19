package de.upb.fsmi;

import android.annotation.*;
import android.content.res.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.view.*;

import com.googlecode.androidannotations.annotations.*;

import de.upb.fsmi.fragments.*;

@EActivity(R.layout.activity_with_drawer)
public class FSDroid extends ActionBarActivity implements DrawerActivity {
	static final String TAG = FSDroid.class.getSimpleName();

	DrawerLayout mDrawerLayout;

	ActionBarDrawerToggle mDrawerToggle;

	private Fragment displayedFragment = null;

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_PROGRESS);
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	}

	@AfterViews
	void init() {
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		initDrawer();

		initStartContent();
	}

	private void initStartContent() {
		switchContentTo(new OverviewFragment_());
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

	@Click({ R.id.drawerItemNews, R.id.drawerItemMisc, R.id.drawerItemContact,
			R.id.drawerItemAbout, R.id.drawerItemMap, R.id.drawerItemMisc })
	void navigationDrawerElementsClicked(View v) {
		DrawerNavigationHelper.navigate(this, v);
	}

	@SuppressLint("InlinedApi")
	@OptionsItem(android.R.id.home)
	void toggleDrawer() {
		if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			closeDrawer();
		} else {
			mDrawerLayout.openDrawer(Gravity.LEFT);
		}
	}

	private void closeDrawer() {
		mDrawerLayout.closeDrawer(Gravity.LEFT);
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

	@Override
	public void switchContentTo(Fragment fragment) {
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
