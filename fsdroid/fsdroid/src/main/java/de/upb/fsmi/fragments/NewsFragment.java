package de.upb.fsmi.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.fima.cardsui.views.CardUI;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import de.upb.fsmi.R;
import de.upb.fsmi.cards.MeetingCard;
import de.upb.fsmi.cards.StatusCard;
import de.upb.fsmi.helper.DataKeeper;
import de.upb.fsmi.helper.NoAvailableNetworkException;
import de.upb.fsmi.helper.UpdateCompletedListener;
import de.upb.fsmi.receivers.UpdateCompletedReceiver;

@EFragment(R.layout.fragment_news)
@OptionsMenu(R.menu.menu_main)
public class NewsFragment extends Fragment implements
		UpdateCompletedListener{

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

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
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
