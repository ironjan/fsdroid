package com.github.ironjan.fsupb.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.fima.cardsui.objects.Card;
import com.github.ironjan.fsupb.cards.views.ContactCardView;
import com.github.ironjan.fsupb.cards.views.ContactCardView_;

public class ContactCard extends Card {

	@Override
	public View getCardContent(Context pContext) {
		ContactCardView view = ContactCardView_.build(pContext);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		return view;
	}

}
