package de.upb.fsmi.fragments;

import android.view.*;

import com.fima.cardsui.objects.*;
import com.fima.cardsui.objects.Card.OnCardSwiped;

public class StatusCardSwipeListener implements OnCardSwiped {

	private final NewsFragment newsFragment;

	public StatusCardSwipeListener(NewsFragment newsFragment) {
		this.newsFragment = newsFragment;
	}

	@Override
	public void onCardSwiped(Card card, View layout) {
		newsFragment.setStatusCardHidden(true);
	}

}
