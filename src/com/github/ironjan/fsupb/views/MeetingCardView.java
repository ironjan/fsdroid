package com.github.ironjan.fsupb.views;

import java.text.*;
import java.util.*;

import android.content.*;
import android.util.*;
import android.widget.*;

import com.github.ironjan.fsupb.*;
import com.googlecode.androidannotations.annotations.*;

@EViewGroup(R.layout.card_meeting)
public class MeetingCardView extends RelativeLayout implements CustomView<Date> {
	@ViewById
	TextView txtMeetingDate;

	public MeetingCardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void bind(Date t) {
		DateFormat sdf = DateFormat.getDateTimeInstance();
		txtMeetingDate.setText(sdf.format(t));
	}

}
