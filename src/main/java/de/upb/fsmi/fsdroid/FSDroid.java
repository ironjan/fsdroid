package de.upb.fsmi.fsdroid;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.fsmi.fsdroid.fragments.OverviewFragment_;
import de.upb.fsmi.fsdroid.sync.AccountCreator;

@SuppressLint("Registered")
@EActivity(R.layout.activity_with_drawer)
public class FSDroid extends ActionBarActivity implements DrawerActivity {
    static final String TAG = FSDroid.class.getSimpleName();
    private Logger LOGGER = LoggerFactory.getLogger(TAG);

    DrawerLayout mDrawerLayout;

    ActionBarDrawerToggle mDrawerToggle;

    private Fragment displayedFragment = null;

    public static final String AUTHORITY = AccountCreator.getAuthority();
    Account mAccount;


    @InstanceState
    int mSelectedContent = 0;

    @Bean
    AccountCreator mAccountCreator;

    @Override

    protected void onCreate(Bundle pSavedInstanceState) {
        if (BuildConfig.DEBUG) LOGGER.debug("onCreate({})", pSavedInstanceState);

        super.onCreate(pSavedInstanceState);
        initActionBar();
        initAccount();

        if (BuildConfig.DEBUG) LOGGER.debug("onCreate({}) done", pSavedInstanceState);
    }

    @AfterViews
    void init() {
        if (BuildConfig.DEBUG) LOGGER.debug("init()");
        initStartContent();
        initDrawer();
        if (BuildConfig.DEBUG) LOGGER.debug("init() done");
    }


    void initActionBar() {
        if (BuildConfig.DEBUG) LOGGER.debug("initActionBar()");

        supportRequestWindowFeature(Window.FEATURE_PROGRESS);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminate(true);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (BuildConfig.DEBUG) LOGGER.debug("initActionBar() done");
    }

    @Background
    void initAccount() {
        if (BuildConfig.DEBUG) LOGGER.debug("initAccount()");

        mAccount = mAccountCreator.getAccountRegisterAccount();
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, new Bundle(), BuildConfig.SYNC_INTERVAL);

        if (BuildConfig.DEBUG) LOGGER.debug("initAccount() done");
    }

    @Trace
    void initStartContent() {
        if (BuildConfig.DEBUG) LOGGER.debug("initStartContent()");

        if (mSelectedContent == 0) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new OverviewFragment_());
            getSupportActionBar().setNavigationMode(
                    ActionBar.NAVIGATION_MODE_STANDARD);
            ft.commit();
        }

        if (BuildConfig.DEBUG) LOGGER.debug("initStartContent() done");
    }

    @Trace
    void initDrawer() {
        if (BuildConfig.DEBUG) LOGGER.debug("initDrawer()");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        mDrawerToggle = new DrawerOpenCloseListener(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawerOpen, R.string.drawerClose);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (BuildConfig.DEBUG) LOGGER.debug("initDrawer() done");
    }

    @Click({R.id.drawerItemNews, R.id.drawerItemMisc, R.id.drawerItemContact,
            R.id.drawerItemAbout, R.id.drawerItemMap, R.id.drawerItemMisc})
    @Trace
    void navigationDrawerElementsClicked(View v) {
        if (BuildConfig.DEBUG) LOGGER.debug("navigationDrawerElementsClicked({})", v);

        DrawerNavigationHelper.navigate(this, v);

        if (BuildConfig.DEBUG) LOGGER.debug("navigationDrawerElementsClicked({}) done", v);
    }

    @SuppressLint("InlinedApi")
    @OptionsItem(android.R.id.home)
    @Trace
    void toggleDrawer() {
        if (BuildConfig.DEBUG) LOGGER.debug("toggleDrawer()");

        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            closeDrawer();
        } else {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }

        if (BuildConfig.DEBUG) LOGGER.debug("toggleDrawer() done");
    }

    @Trace
    void closeDrawer() {
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

    @Trace
    @Override
    public void switchContentTo(Fragment fragment) {
        if (BuildConfig.DEBUG) LOGGER.debug("switchContentTo({})", fragment);

        if (displayedFragment == fragment) {
            if (BuildConfig.DEBUG) Log.v(TAG, "Fragments were the same. Not switching.");
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        getSupportActionBar().setNavigationMode(
                ActionBar.NAVIGATION_MODE_STANDARD);
        ft.commit();
        closeDrawer();
        mSelectedContent++;

        if (BuildConfig.DEBUG) LOGGER.debug("switchContentTo({})", fragment);
    }
}
