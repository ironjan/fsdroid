package de.upb.fsmi.cards;

import android.content.*;
import android.view.*;
import android.view.ViewGroup.LayoutParams;

import com.fima.cardsui.objects.*;

import de.upb.fsmi.cards.views.*;

public class ContactCard extends Card {

	@Override
	public View getCardContent(Context pContext) {
		ContactCardView view = ContactCardView_.build(pContext);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		return view;
	}

}
