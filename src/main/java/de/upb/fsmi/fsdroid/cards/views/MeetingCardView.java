package de.upb.fsmi.fsdroid.cards.views;

import android.content.*;
import android.util.*;
import android.widget.*;

import org.androidannotations.annotations.*;
import org.slf4j.*;

import java.text.*;
import java.util.*;

import de.upb.fsmi.fsdroid.*;

@EViewGroup(R.layout.card_meeting)
public class MeetingCardView extends RelativeLayout implements CustomView<Date> {

    @ViewById
    TextView txtMeetingDate;

    private final static Logger LOGGER = LoggerFactory
            .getLogger(MeetingCardView.class);

    public MeetingCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //
    @Override
    public void bind(Date t) {
        LOGGER.debug("Binding date = {}", t);
        if (t != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
            txtMeetingDate.setText(sdf.format(t));
            LOGGER.trace("Displayed date");
        } else {
            txtMeetingDate.setText("Datum ist unbekannt...");
            LOGGER.trace("date was null");
        }
        invalidate();
    }
}
