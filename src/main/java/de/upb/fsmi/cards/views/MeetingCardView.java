package de.upb.fsmi.cards.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.upb.fsmi.R;

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
