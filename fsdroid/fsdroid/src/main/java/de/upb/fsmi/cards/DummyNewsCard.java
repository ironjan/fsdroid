package de.upb.fsmi.cards;

import android.content.*;
import android.view.*;
import android.view.ViewGroup.LayoutParams;

import com.fima.cardsui.objects.*;

import de.upb.fsmi.cards.views.*;

public class DummyNewsCard extends Card {

	@Override
	public View getCardContent(Context pContext) {
		DummyNewsCardView view = DummyNewsCardView_.build(pContext);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		return view;
	}

}
