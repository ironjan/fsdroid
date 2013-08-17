package de.upb.fsmi.cards;

import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.fima.cardsui.objects.Card;

import de.upb.fsmi.cards.views.MeetingCardView;
import de.upb.fsmi.cards.views.MeetingCardView_;

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
