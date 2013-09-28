package de.upb.fsmi.fragments;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

import com.fima.cardsui.views.CardUI;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

import de.upb.fsmi.R;
import de.upb.fsmi.cards.DummyNewsCard;
import de.upb.fsmi.helper.DataKeeper;
import de.upb.fsmi.helper.UpdateCompletedListener;
import de.upb.fsmi.receivers.UpdateCompletedReceiver;

@EFragment(R.layout.fragment_news)
@OptionsMenu(R.menu.menu_main)
public class NewsFragment extends Fragment implements UpdateCompletedListener {

	@ViewById
	CardUI cardsview;

	@Bean
	DataKeeper dataKeeper;

	@FragmentById
	StatusViewFragment fragmentStatus;
	@FragmentById
	MeetingDateFragment fragmentMeeting;

	UpdateCompletedReceiver updateCompletedReceiver = new UpdateCompletedReceiver(
			this);

	private DummyNewsCard dummyNewsCard;

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
		cardsview.setSwipeable(false);

		dummyNewsCard = new DummyNewsCard();

		cardsview.addCard(dummyNewsCard);

		refreshDisplayedData();
	}

	@UiThread
	protected void refreshDisplayedData() {
		cardsview.refresh();
	}

	@Override
	public void updateCompleted() {
		refreshDisplayedData();
	}

}
