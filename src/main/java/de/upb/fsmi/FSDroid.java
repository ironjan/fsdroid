package de.upb.fsmi;

import android.accounts.*;
import android.annotation.*;
import android.content.*;
import android.content.res.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;

import org.androidannotations.annotations.*;
import org.slf4j.*;

import de.upb.fsmi.fragments.*;
import de.upb.fsmi.sync.*;

@EActivity(R.layout.activity_with_drawer)
public class FSDroid extends ActionBarActivity implements DrawerActivity {
    static final String TAG = FSDroid.class.getSimpleName();
    private Logger LOGGER = LoggerFactory.getLogger(TAG);

    DrawerLayout mDrawerLayout;

    ActionBarDrawerToggle mDrawerToggle;

    private Fragment displayedFragment = null;

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "de.ironjan.provider";
    // Instance fields
    Account mAccount;

    @Bean
    AccountCreator mAccountCreator;

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        if (BuildConfig.DEBUG) LOGGER.debug("onCreate({})", pSavedInstanceState);

        super.onCreate(pSavedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_PROGRESS);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        if (BuildConfig.DEBUG) LOGGER.debug("onCreate({}) done", pSavedInstanceState);
    }

    @AfterViews
    void init() {
        if (BuildConfig.DEBUG) LOGGER.debug("init()");
        initActionBar();
        initDrawer();
        initAccount();
        initStartContent();
        if (BuildConfig.DEBUG) LOGGER.debug("init() done");
    }

    private void initActionBar() {
        if (BuildConfig.DEBUG) LOGGER.debug("initActionBar()");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (BuildConfig.DEBUG) LOGGER.debug("initActionBar() done");
    }

    private void initAccount() {
        if (BuildConfig.DEBUG) LOGGER.debug("initAccount()");

        mAccount = mAccountCreator.create(this);
        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, new Bundle(), BuildConfig.SYNC_INTERVAL);

        if (BuildConfig.DEBUG) LOGGER.debug("initAccount() done");
    }

    private void initStartContent() {
        if (BuildConfig.DEBUG) LOGGER.debug("initStartContent()");

        switchContentTo(new OverviewFragment_());

        if (BuildConfig.DEBUG) LOGGER.debug("initStartContent() done");
    }

    private void initDrawer() {
        if (BuildConfig.DEBUG) LOGGER.debug("initDrawer()");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        mDrawerToggle = new DrawerOpenCloseListener(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (BuildConfig.DEBUG) LOGGER.debug("initDrawer() done");
    }

    @Click({R.id.drawerItemNews, R.id.drawerItemMisc, R.id.drawerItemContact,
            R.id.drawerItemAbout, R.id.drawerItemMap, R.id.drawerItemMisc})
    void navigationDrawerElementsClicked(View v) {
        if (BuildConfig.DEBUG) LOGGER.debug("navigationDrawerElementsClicked({})", v);

        DrawerNavigationHelper.navigate(this, v);

        if (BuildConfig.DEBUG) LOGGER.debug("navigationDrawerElementsClicked({}) done", v);
    }

    @SuppressLint("InlinedApi")
    @OptionsItem(android.R.id.home)
    void toggleDrawer() {
        if (BuildConfig.DEBUG) LOGGER.debug("toggleDrawer()");

        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            closeDrawer();
        } else {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }

        if (BuildConfig.DEBUG) LOGGER.debug("toggleDrawer() done");
    }

    private void closeDrawer() {
        if (BuildConfig.DEBUG) LOGGER.debug("closeDrawer()");

        mDrawerLayout.closeDrawer(Gravity.LEFT);

        if (BuildConfig.DEBUG) LOGGER.debug("closeDrawer() done");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) LOGGER.debug("onPostCreate({})", savedInstanceState);

        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();

        if (BuildConfig.DEBUG) LOGGER.debug("onPostCreate({})", savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (BuildConfig.DEBUG) LOGGER.debug("onConfigurationChanged({})", newConfig);

        super.onConfigurationChanged(newConfig);
        mDrawerToggle.syncState();

        if (BuildConfig.DEBUG) LOGGER.debug("onConfigurationChanged({})", newConfig);
    }

    @Override
    public void switchContentTo(Fragment fragment) {
        if (BuildConfig.DEBUG) LOGGER.debug("switchContentTo({})", fragment);

        if (displayedFragment == fragment) {
            if (BuildConfig.DEBUG) Log.v(TAG, "Fragments were the same. Not switching.");
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.content_frame, fragment);
        getSupportActionBar().setNavigationMode(
                ActionBar.NAVIGATION_MODE_STANDARD);
        ft.commit();
        closeDrawer();

        if (BuildConfig.DEBUG) LOGGER.debug("switchContentTo({})", fragment);
    }
}
