package com.github.ironjan.fsupb.fragments;

import android.view.*;

import com.fima.cardsui.objects.*;
import com.fima.cardsui.objects.Card.OnCardSwiped;

public class MeetingCardSwipeListener implements OnCardSwiped {

	private final NewsFragment newsFragment;

	public MeetingCardSwipeListener(NewsFragment newsFragment) {
		this.newsFragment = newsFragment;
	}

	@Override
	public void onCardSwiped(Card card, View layout) {
		newsFragment.setMeetingCardHidden(true);
	}

}
