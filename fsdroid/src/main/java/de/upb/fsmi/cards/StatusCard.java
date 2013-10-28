package de.upb.fsmi.cards;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.fima.cardsui.objects.Card;

import de.upb.fsmi.cards.views.StatusCardView;
import de.upb.fsmi.cards.views.StatusCardView_;

public class StatusCard extends Card {

	private Integer status = Integer.valueOf(-1);

	public StatusCard(Integer status) {
		super();
		Log.v(StatusCard.class.getSimpleName(), "constructed");
		this.status = status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public View getCardContent(Context context) {
		Log.v(StatusCard.class.getSimpleName(), "getCardContent");
		final StatusCardView view = StatusCardView_.build(context, null, 0);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		view.bind(status);
		Log.v(StatusCard.class.getSimpleName(), "getCardContent " + view);

		return view;
	}

}
