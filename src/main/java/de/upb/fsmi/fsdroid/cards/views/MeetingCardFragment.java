package de.upb.fsmi.fsdroid.cards.views;


import android.database.*;
import android.os.*;
import android.support.v4.app.*;
import android.widget.*;

import org.androidannotations.annotations.*;
import org.slf4j.*;

import de.upb.fsmi.fsdroid.*;
import de.upb.fsmi.fsdroid.sync.*;
import de.upb.fsmi.fsdroid.sync.entities.*;

@EFragment(R.layout.card_meeting)
public class MeetingCardFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @ViewById
    TextView txtMeetingDate;

    private static final int MEETING_LOADER = 0x01;

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (BuildConfig.DEBUG) LOGGER.debug("onCreateLoader({},{})", i, bundle);
        String[] projection = {MeetingDate.COLUMN_VALUE, MeetingDate.COLUMN_ID};
        android.support.v4.content.CursorLoader cursorLoader = new android.support.v4.content.CursorLoader(getActivity(),
                FSDroidContentProvider.MEETING_DATE_URI, projection, null, null, MeetingDate.COLUMN_LAST_UPDATE + " DESC");

        if (BuildConfig.DEBUG) LOGGER.debug("onCreateLoader({},{}) done", i, bundle);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> cursorLoader, Cursor cursor) {
        if (BuildConfig.DEBUG) LOGGER.debug("onLoadFinished({},{})", cursor, cursorLoader);

        if (cursor.moveToFirst()) {
            if (BuildConfig.DEBUG) LOGGER.debug("moved to first");
            if (cursor.getColumnCount() > 0) {
                bind(cursor.getString(0));
            }
        }

        if (BuildConfig.DEBUG) LOGGER.debug("onLoadFinished({},{}) done", cursor, cursorLoader);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> cursorLoader) {
        // do nothing
        if (BuildConfig.DEBUG) LOGGER.debug("onLoaderReset({}) done", cursorLoader);
    }

    private final static Logger LOGGER = LoggerFactory
            .getLogger(MeetingCardFragment.class.getSimpleName());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(MEETING_LOADER, null, this);

        if (BuildConfig.DEBUG) LOGGER.debug("onCreate({}) done", savedInstanceState);
    }

    public void bind(String dateString) {
        LOGGER.debug("Binding date = {}", dateString);
        if (dateString != null) {
            txtMeetingDate.setText(dateString);
            LOGGER.trace("Displayed date");
        } else {
            txtMeetingDate.setText("Datum ist unbekannt...");
            LOGGER.trace("date was null");
        }
    }
}
