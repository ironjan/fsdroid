package de.upb.fsmi.fragments;

import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.fima.cardsui.views.*;
import com.googlecode.androidannotations.annotations.*;

import de.upb.fsmi.*;
import de.upb.fsmi.cards.*;
import de.upb.fsmi.helper.*;
import de.upb.fsmi.receivers.*;

@EFragment(R.layout.fragment_news)
@OptionsMenu(R.menu.menu_main)
public class NewsFragment extends Fragment implements UpdateCompletedListener {

	private static final String TAG = NewsFragment.class.getSimpleName();

	@ViewById
	CardUI cardsview;

	@Bean
	DataKeeper dataKeeper;

	UpdateCompletedReceiver updateCompletedReceiver = new UpdateCompletedReceiver(this);

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
		updateCompletedReceiver.registerReceiver(getActivity().getApplicationContext());
		super.onResume();
	}

	@Override
	public void onPause() {
		updateCompletedReceiver.unregisterReceiver();
		super.onPause();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@AfterViews
	@UiThread
	protected void initCardView() {
		cardsview.setSwipeable(false);

		statusCard = new StatusCard(dataKeeper.getFsmiState());
		meetingCard = new MeetingCard(dataKeeper.getNextMeetingDate());

		cardsview.addCard(statusCard);
		cardsview.addCard(meetingCard);

		statusCard.setOnCardSwipedListener(new StatusCardSwipeListener(this));
		meetingCard.setOnCardSwipedListener(new MeetingCardSwipeListener(this));

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
