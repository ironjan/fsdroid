package de.upb.fsmi.fsdroid.cards.views;

import android.content.*;
import android.database.*;
import android.util.*;
import android.widget.*;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.res.*;
import org.slf4j.*;

import de.upb.fsmi.fsdroid.*;
import de.upb.fsmi.fsdroid.sync.*;
import de.upb.fsmi.fsdroid.sync.entities.*;

@EViewGroup(R.layout.card_status)
public class StatusCardViewGroup extends RelativeLayout {

    private static final int STATUS_LOADER = 0x01;
    private final Logger LOGGER = LoggerFactory.getLogger(StatusCardViewGroup.class.getSimpleName());

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
    private Context mContext;

    public StatusCardViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }


    @AfterViews
    @UiThread
    void loadStatus() {
        String[] projection = {Status.COLUMN_VALUE, Status.COLUMN_ID};

        final Cursor cursor = mContext.getContentResolver().query(
                FSDroidContentProvider.STATUS_URI, projection, null, null, Status.COLUMN_LAST_UPDATE + " DESC");

        if (cursor.moveToFirst()) {
            if (BuildConfig.DEBUG) LOGGER.debug("moved to first");
            if (cursor.getColumnCount() > 0) {
                bind(cursor.getInt(0));
            }
        }
    }


    public void bind(Integer status) {
        Log.v(StatusCardViewGroup.class.getSimpleName(), "Binding status=" + status);

        final int statusInt = status.intValue();

        imgStatus.setImageLevel(statusInt);
        txtStatus.setText(stati[statusInt]);
        txtDescriptionStatus.setText(statiDescriptions[statusInt]);
    }
}
