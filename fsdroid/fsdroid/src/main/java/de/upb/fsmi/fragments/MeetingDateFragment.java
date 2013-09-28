package de.upb.fsmi.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.fima.cardsui.views.CardUI;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

import de.upb.fsmi.R;
import de.upb.fsmi.cards.MeetingCard;
import de.upb.fsmi.helper.DataKeeper;
import de.upb.fsmi.helper.UpdateCompletedListener;
import de.upb.fsmi.receivers.UpdateCompletedReceiver;

@EFragment(R.layout.fragment_meeting_date)
public class MeetingDateFragment extends Fragment implements
		UpdateCompletedListener {

	private static final String TAG = MeetingDateFragment.class.getSimpleName();

	@ViewById
	CardUI meetingCardsview;

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
		meetingCardsview.setSwipeable(false);

		meetingCard = new MeetingCard(dataKeeper.getNextMeetingDate());
		meetingCardsview.addCard(meetingCard);

		refreshDisplayedData();
	}

	@UiThread
	protected void refreshDisplayedData() {
		refreshMeetingCard();
		meetingCardsview.refresh();
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
