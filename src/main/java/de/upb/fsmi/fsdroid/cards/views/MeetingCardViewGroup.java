package de.upb.fsmi.fsdroid.cards.views;


import android.content.*;
import android.database.*;
import android.util.*;
import android.widget.*;

import org.androidannotations.annotations.*;
import org.slf4j.*;

import de.upb.fsmi.fsdroid.*;
import de.upb.fsmi.fsdroid.sync.*;
import de.upb.fsmi.fsdroid.sync.entities.*;

@EViewGroup(R.layout.card_meeting)
public class MeetingCardViewGroup extends RelativeLayout {

    private final Context mContext;
    @ViewById
    TextView txtMeetingDate;

    private final static Logger LOGGER = LoggerFactory
            .getLogger(MeetingCardViewGroup.class.getSimpleName());

    public MeetingCardViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @AfterViews
    @Background
    void loadDate() {
        final ContentResolver contentResolver = mContext.getContentResolver();
        String[] projection = {MeetingDate.COLUMN_VALUE, MeetingDate.COLUMN_ID};
        final Cursor query = contentResolver.query(FSDroidContentProvider.MEETING_DATE_URI, projection, null, null, MeetingDate.COLUMN_LAST_UPDATE + " DESC");

        if (query.getCount() > 0) {
            query.moveToFirst();
            if (query.getColumnCount() > 0) {
                bind(query.getString(0));
            }
        }
        query.close();
    }


    @UiThread
    void bind(String dateString) {
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
