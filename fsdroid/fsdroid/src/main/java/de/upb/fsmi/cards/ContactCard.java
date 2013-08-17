package de.upb.fsmi.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.fima.cardsui.objects.Card;

import de.upb.fsmi.cards.views.ContactCardView;
import de.upb.fsmi.cards.views.ContactCardView_;

public class ContactCard extends Card {

	@Override
	public View getCardContent(Context pContext) {
		ContactCardView view = ContactCardView_.build(pContext);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		return view;
	}

}
