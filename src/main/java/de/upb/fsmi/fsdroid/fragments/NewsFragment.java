package de.upb.fsmi.fsdroid.fragments;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.SyncInfo;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.res.StringRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.fsmi.fsdroid.BuildConfig;
import de.upb.fsmi.fsdroid.R;
import de.upb.fsmi.fsdroid.SingleNews_;
import de.upb.fsmi.fsdroid.sync.AccountCreator;
import de.upb.fsmi.fsdroid.sync.NewsItemContract;

@EFragment(R.layout.fragment_news)
@OptionsMenu(R.menu.menu_main)
public class NewsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, SyncStatusObserver {
    private static final int NEWS_LOADER = 0x01;
    private SimpleCursorAdapter adapter;

    private final Logger LOGGER = LoggerFactory.getLogger(NewsFragment.TAG);

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (BuildConfig.DEBUG) LOGGER.debug("");
        String[] projection = NewsItemContract.NEWS_LIST_PROJECTION;
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                NewsItemContract.NEWS_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }

    private static final String TAG = NewsFragment.class.getSimpleName();

    @Bean
    AccountCreator mAccountCreator;

    @StringRes
    String news;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) LOGGER.debug("onCreate(..)");
        super.onCreate(savedInstanceState);


        String[] uiBindFrom = NewsItemContract.NEWS_LIST_PROJECTION;
        int[] uiBindTo = {R.id.newsHeadline, R.id.newsText};
        getLoaderManager().initLoader(NEWS_LOADER, null, this);
        adapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(), R.layout.card_news_item,
                null, uiBindFrom, uiBindTo, 0);
        setListAdapter(adapter);

        if (BuildConfig.DEBUG) LOGGER.debug("onCreate(..) done");
    }

    @ItemClick(android.R.id.list)
    void click(int position) {
        if (BuildConfig.DEBUG) LOGGER.debug("click(..)");
        long _id = adapter.getItemId(position);
        SingleNews_.intent(getActivity()).news_id(_id).start();
        if (BuildConfig.DEBUG) LOGGER.debug("click(..) done");
    }

    @OptionsItem(R.id.ab_refresh)
    void refresh() {
        if (BuildConfig.DEBUG) LOGGER.debug("refresh()");

        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(mAccountCreator.getAccountRegisterAccount(), AccountCreator.getAuthority(), settingsBundle);

        if (BuildConfig.DEBUG) LOGGER.debug("refresh() done");
    }

    @Override
    public void onResume() {
        if (BuildConfig.DEBUG) LOGGER.debug("onResume()");
        super.onResume();
        int mask = ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE | ContentResolver.SYNC_OBSERVER_TYPE_PENDING | ContentResolver.SYNC_OBSERVER_TYPE_SETTINGS;
        ContentResolver.addStatusChangeListener(mask, this);
        if (BuildConfig.DEBUG) LOGGER.debug("onResume() done");
    }

    @Override
    public void onStatusChanged(int status) {
        if (BuildConfig.DEBUG) LOGGER.debug("onStatusChanged({})", status);
        boolean myAccountSyncing = getSyncStatus();
        updateSyncStatus(myAccountSyncing);
        if (BuildConfig.DEBUG) LOGGER.debug("onStatusChanged({}) done", status);
    }

    private boolean getSyncStatus() {
        if (BuildConfig.DEBUG) LOGGER.debug("getSyncStatus()");
        final String account = AccountCreator.getAccountName();
        final String authority = AccountCreator.getAuthority();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            boolean result = isSyncActiveHoneycomb(account, authority);
            if (BuildConfig.DEBUG) LOGGER.debug("getSyncStatus() -> {}", result);
            return result;
        } else {
            SyncInfo currentSync = ContentResolver.getCurrentSync();
            boolean result = currentSync != null && currentSync.account.equals(account) &&
                    currentSync.authority.equals(authority);
            if (BuildConfig.DEBUG) LOGGER.debug("getSyncStatus() -> {}", result);
            return result;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private boolean isSyncActiveHoneycomb(String accountName, String authority) {
        if (BuildConfig.DEBUG) LOGGER.debug("isSyncActiveHoneycomb({},{})", accountName, authority);

        for (SyncInfo syncInfo : ContentResolver.getCurrentSyncs()) {
            if (syncInfo.account.name.equals(accountName) &&
                    syncInfo.authority.equals(authority)) {
                if (BuildConfig.DEBUG)
                    LOGGER.debug("isSyncActiveHoneycomb({},{}) done", accountName, authority);
                return true;
            }
        }

        if (BuildConfig.DEBUG)
            LOGGER.debug("isSyncActiveHoneycomb({},{}) done", accountName, authority);
        return false;
    }

    @UiThread
    void updateSyncStatus(boolean myAccountSyncing) {
        if (BuildConfig.DEBUG) LOGGER.debug("updateSyncStatus({})", myAccountSyncing);
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        if (activity == null)
            return;
        activity.setProgressBarVisibility(myAccountSyncing);
        activity.setProgressBarIndeterminateVisibility(myAccountSyncing);
        if (BuildConfig.DEBUG) LOGGER.debug("updateSyncStatus({}) done", myAccountSyncing);
    }
}
