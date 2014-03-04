package de.upb.fsmi.fsdroid.cards.views;

import android.accounts.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.*;
import android.widget.*;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.res.*;
import org.slf4j.*;

import de.upb.fsmi.fsdroid.*;
import de.upb.fsmi.fsdroid.sync.*;
import de.upb.fsmi.fsdroid.sync.entities.*;

@EFragment(R.layout.card_status)
public class StatusCardFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int STATUS_LOADER = 0x01;
    private final Logger LOGGER = LoggerFactory.getLogger(StatusCardFragment.class.getSimpleName());

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (BuildConfig.DEBUG) LOGGER.debug("onCreateLoader({},{})", i, bundle);
        String[] projection = {Status.COLUMN_VALUE, Status.COLUMN_ID};
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                FSDroidContentProvider.STATUS_URI, projection, null, null, Status.COLUMN_LAST_UPDATE + " DESC");

        if (BuildConfig.DEBUG) LOGGER.debug("onCreateLoader({},{}) done", i, bundle);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (BuildConfig.DEBUG) LOGGER.debug("onLoadFinished({},{})", cursor, cursorLoader);

        if (cursor.moveToFirst()) {
            if (BuildConfig.DEBUG) LOGGER.debug("moved to first");
            if (cursor.getColumnCount() > 0) {
                bind(cursor.getInt(0));
            }
        }

        if (BuildConfig.DEBUG) LOGGER.debug("onLoadFinished({},{}) done", cursor, cursorLoader);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        // do nothing
        if (BuildConfig.DEBUG) LOGGER.debug("onLoaderReset({}) done", cursorLoader);
    }


    @ViewById(R.id.txtStatus)
    TextView txtStatus;

    @ViewById(R.id.txtDescriptionStatus)
    TextView txtDescriptionStatus;

    @ViewById(R.id.imgStatus)
    ImageView imgStatus;

    @StringArrayRes
    String[] stati, statiDescriptions;

    @Bean
    AccountCreator mAccountCreator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(STATUS_LOADER, null, this);

        if (BuildConfig.DEBUG) LOGGER.debug("onCreate({}) done", savedInstanceState);
    }


    public void bind(Integer status) {
        Log.v(StatusCardFragment.class.getSimpleName(), "Binding status=" + status);

        final int statusInt = status.intValue();

        imgStatus.setImageLevel(statusInt);
        txtStatus.setText(stati[statusInt]);
        txtDescriptionStatus.setText(statiDescriptions[statusInt]);
    }

    @Override
    public void onResume() {
        super.onResume();

        Account account = mAccountCreator.getAccountRegisterAccount();
        String authority = AccountCreator.getAuthority();
        ContentResolver resolver = getActivity().getContentResolver();
        // Turn on automatic syncing for the default account and authority
        resolver.setSyncAutomatically(account, authority, true);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
