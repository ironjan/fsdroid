package de.upb.fsmi.fragments;

import java.util.*;

import android.support.v4.app.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.fima.cardsui.views.*;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.*;

import de.upb.fsmi.*;
import de.upb.fsmi.cards.*;
import de.upb.fsmi.helper.*;

@EFragment(R.layout.fragment_overview)
public class OverviewFragment extends Fragment {
	@StringRes
	String misc;

	private static final String TAG = OverviewFragment.class.getSimpleName();

	@AfterViews
	void updateTitle() {
		((ActionBarActivity) getActivity()).getSupportActionBar()
				.setTitle(misc);
	}

	@ViewById
	CardUI cardsView;

	@Bean
	DataKeeper dataKeeper;

	private MeetingCard meetingCard;

	private StatusCard statusCard;

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

		statusCard = new StatusCard(dataKeeper.getFsmiState());

		cardsView.addCard(statusCard);

		refreshDisplayedData();
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
		refreshStatusCard();
		refreshMeetingCard();
		cardsView.refresh();
	}

	void refreshStatusCard() {
		statusCard.setStatus(dataKeeper.getFsmiState());
	}

	void refreshMeetingCard() {
		meetingCard.setDate(dataKeeper.getNextMeetingDate());
	}

	void logError(Exception e) {
		Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
		Log.e(TAG, e.getMessage(), e);
	}

}
