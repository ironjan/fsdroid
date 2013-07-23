package com.github.ironjan.fsupb.fragments;

import android.util.*;
import android.widget.*;

import com.actionbarsherlock.app.*;
import com.fima.cardsui.objects.*;
import com.fima.cardsui.views.*;
import com.github.ironjan.fsupb.*;
import com.github.ironjan.fsupb.cards.*;
import com.github.ironjan.fsupb.model.*;
import com.github.ironjan.fsupb.receiver.*;
import com.googlecode.androidannotations.annotations.*;

@EFragment(R.layout.fragment_news)
public class NewsFragment extends SherlockFragment implements
		UpdateCompletedListener {

	private static final String TAG = NewsFragment.class.getSimpleName();

	@ViewById
	CardUI cardsview;

	@Bean
	DataKeeper dataKeeper;

	UpdateCompletedReceiver updateCompletedReceiver = new UpdateCompletedReceiver(
			this);

	private StatusCard statusCard;
	private MeetingCard meetingCard;

	private boolean statusCardHidden = false, meetingCardHidden = false;

	public void setStatusCardHidden(boolean statusCardHidden) {
		this.statusCardHidden = statusCardHidden;
	}

	public void setMeetingCardHidden(boolean meetingCardHidden) {
		this.meetingCardHidden = meetingCardHidden;
	}

	@Override
	public void onResume() {
		updateCompletedReceiver.registerReceiver(getActivity()
				.getApplicationContext());
		super.onResume();
	}

	@Override
	public void onPause() {
		updateCompletedReceiver.unregisterReceiver();
		super.onPause();
	}

	@AfterViews
	@UiThread
	protected void initCardView() {
		cardsview.setSwipeable(true);

		statusCard = new StatusCard(dataKeeper.getFsmiState());
		meetingCard = new MeetingCard(dataKeeper.getNextMeetingDate());

		cardsview.addCard(statusCard);
		cardsview.addCard(meetingCard);

		statusCard.setOnCardSwipedListener(new StatusCardSwipeListener(this));
		meetingCard.setOnCardSwipedListener(new MeetingCardSwipeListener(this));

		cardsview.addCard(new TestCard(-1, -1));

		int s = 0;
		for (int i = 0; i < 10; i++) {
			if (i % 3 == 0) {
				cardsview.addStack(new CardStack());
				s++;
			}
			cardsview.addCardToLastStack(new TestCard(i, s));
		}
		refreshDisplayedData();
	}

	@UiThread
	protected void refreshDisplayedData() {
		refreshStatusCard();
		refreshMeetingCard();
		cardsview.refresh();
	}

	void refreshMeetingCard() {
		meetingCard.setDate(dataKeeper.getNextMeetingDate());
		if (meetingCardHidden) {
			cardsview.addCard(meetingCard);
			setMeetingCardHidden(false);
		}
	}

	void refreshStatusCard() {
		statusCard.setStatus(dataKeeper.getFsmiState());
		if (statusCardHidden) {
			cardsview.addCard(statusCard);
			setStatusCardHidden(false);
		}
	}

	void logError(Exception e) {
		Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
		Log.e(TAG, e.getMessage(), e);
	}

	@Override
	public void updateCompleted() {
		refreshDisplayedData();
	}

}
