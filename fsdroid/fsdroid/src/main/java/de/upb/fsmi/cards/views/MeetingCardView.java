package de.upb.fsmi.cards.views;

import java.text.*;
import java.util.*;

import android.content.*;
import android.util.*;
import android.widget.*;

import com.googlecode.androidannotations.annotations.*;

import de.upb.fsmi.*;

@EViewGroup(R.layout.card_meeting)
public class MeetingCardView extends RelativeLayout implements CustomView<Date> {
	@ViewById
	TextView txtMeetingDate;

	public MeetingCardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void bind(Date t) {
		if (t != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy' 'HH:mm");
			txtMeetingDate.setText(sdf.format(t));
		} else {
			txtMeetingDate.setText("Unbekanntes Datum -.-");
		}
	}
}
