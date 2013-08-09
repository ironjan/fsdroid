package com.github.ironjan.fsupb.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.fima.cardsui.objects.Card;
import com.github.ironjan.fsupb.cards.entities.CouncilMember;
import com.github.ironjan.fsupb.cards.views.CouncilMemberCardView;
import com.github.ironjan.fsupb.cards.views.CouncilMemberCardView_;

public class ConcilMemberCard extends Card {

	private CouncilMemberCardView view;
	private CouncilMember cm;

	@Override
	public View getCardContent(Context context) {
		view = CouncilMemberCardView_.build(context);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		view.bind(cm);
		return view;
	}

	public void bind(CouncilMember cm) {
		this.cm = cm;
	}
}
