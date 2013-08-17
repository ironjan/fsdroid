package de.upb.fsmi.cards.views;

import java.text.DateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

import de.upb.fsmi.R;

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
			DateFormat sdf = DateFormat.getDateTimeInstance();
			txtMeetingDate.setText(sdf.format(t));
		} else {
			txtMeetingDate.setText("Unbekanntes Datum -.-");
		}
	}
}
