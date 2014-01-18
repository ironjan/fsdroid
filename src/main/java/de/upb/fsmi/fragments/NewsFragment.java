package de.upb.fsmi.fragments;

import android.content.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.*;
import android.util.*;
import android.view.*;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.res.*;
import org.slf4j.*;

import de.upb.fsmi.*;
import de.upb.fsmi.cards.*;
import de.upb.fsmi.sync.*;

@EFragment(R.layout.fragment_news)
@OptionsMenu(R.menu.menu_main)
public class NewsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int TUTORIAL_LIST_LOADER = 0x01;
    private SimpleCursorAdapter adapter;

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = NewsItemContract.NEWS_LIST_PROJECTION;
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                Uri.parse("content://de.ironjan.provider/news"), projection, null, null, null);
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
    private static final Logger LOGGER = LoggerFactory.getLogger(TAG);
    @Bean
    AccountCreator mAccountCreator;
    @StringRes
    String news;
    private NewsCard dummyNewsCard;
    private MenuItem ab_refresh;
    private boolean mProgressShown;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] uiBindFrom = NewsItemContract.NEWS_LIST_PROJECTION;
        int[] uiBindTo = {R.id.newsHeadline, R.id.newsText};
        getLoaderManager().initLoader(TUTORIAL_LIST_LOADER, null, this);
        adapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(), R.layout.card_news_item,
                null, uiBindFrom, uiBindTo,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setListAdapter(adapter);
    }

    @ItemClick(android.R.id.list)
    void click(int position) {
        long _id = adapter.getItemId(position);
        SingleNews_.intent(getActivity()).news_id(_id).start();
    }

    @OptionsItem(R.id.ab_refresh)
    void refresh() {
        if (BuildConfig.DEBUG) Log.d(TAG, "refresh()");

        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(mAccountCreator.getAccountRegisterAccount(), AccountCreator.getAuthority(), settingsBundle);

        if (BuildConfig.DEBUG) Log.d(TAG, "refresh() done");
    }

}
