package com.github.ironjan.fsupb.cards;

import java.util.*;

import android.content.*;
import android.view.*;
import android.view.ViewGroup.LayoutParams;

import com.fima.cardsui.objects.*;
import com.github.ironjan.fsupb.views.*;

public class MeetingCard extends Card {

	private Date date;

	public MeetingCard(Date date) {
		super();
		this.date = date;
	}

	@Override
	public View getCardContent(Context context) {
		MeetingCardView view = MeetingCardView_.build(context, null, 0);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		view.bind(date);
		return view;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
