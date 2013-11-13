package de.upb.fsmi.cards.views;

import java.text.*;
import java.util.*;

import org.slf4j.*;

import android.content.*;
import android.util.*;
import android.widget.*;

import com.googlecode.androidannotations.annotations.*;

import de.upb.fsmi.*;

@EViewGroup(R.layout.card_meeting)
public class MeetingCardView extends RelativeLayout implements CustomView<Date> {

	@ViewById
	TextView txtMeetingDate;

	private final static Logger LOGGER = LoggerFactory
			.getLogger(MeetingCardView.class);

	public MeetingCardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	//
	@Override
	public void bind(Date t) {
		LOGGER.debug("Binding date = {}", t);
		if (t != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy' 'HH:mm");
			txtMeetingDate.setText(sdf.format(t));
			LOGGER.trace("Displayed date");
		}
		else {
			txtMeetingDate.setText("Datum ist unbekannt...");
			LOGGER.trace("date was null");
		}
	}
}
