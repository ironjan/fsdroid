package de.upb.fsmi.fragments;

import java.util.*;

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

@EFragment(R.layout.empty_view)
public class MeetingDateFragment extends Fragment implements
		UpdateCompletedListener {

	private static final String TAG = MeetingDateFragment.class.getSimpleName();

	CardUI cardsView;

	@Bean
	DataKeeper dataKeeper;

	UpdateCompletedReceiver updateCompletedReceiver = new UpdateCompletedReceiver(
			this);

	private MeetingCard meetingCard;

	@Override
	public void onResume() {
		updateCompletedReceiver.registerReceiver(getActivity()
				.getApplicationContext());
		super.onResume();
		cardsView = (CardUI) getActivity().findViewById(R.id.cardsview);
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
		cardsView.setSwipeable(false);

		Date nextMeetingDate = dataKeeper.getNextMeetingDate();
		if (nextMeetingDate != null) {
			displayDate(nextMeetingDate);
		} else {
			refreshDate();
		}
	}

	@Background
	void refreshDate() {
		try {
			dataKeeper.refresh(false);
		} catch (NoAvailableNetworkException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		Date nextMeetingDate = dataKeeper.getNextMeetingDate();
		if (nextMeetingDate != null) {
			displayDate(nextMeetingDate);
		}
	}

	@UiThread
	void displayDate(Date nextMeetingDate) {
		meetingCard = new MeetingCard(nextMeetingDate);
		cardsView.addCard(meetingCard);

		refreshDisplayedData();
	}

	@UiThread
	protected void refreshDisplayedData() {
		refreshMeetingCard();
		cardsView.refresh();
	}

	void refreshMeetingCard() {
		meetingCard.setDate(dataKeeper.getNextMeetingDate());
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