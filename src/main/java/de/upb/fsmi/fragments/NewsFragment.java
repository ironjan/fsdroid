package de.upb.fsmi.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.res.StringRes;

import de.upb.fsmi.BuildConfig;
import de.upb.fsmi.R;
import de.upb.fsmi.SingleNews_;
import de.upb.fsmi.sync.AccountCreator;
import de.upb.fsmi.sync.NewsItemContract;

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
