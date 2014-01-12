package de.upb.fsmi.fragments;

import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.fima.cardsui.views.*;
import org.androidannotations.annotations.*;

import de.upb.fsmi.*;
import de.upb.fsmi.cards.*;
import de.upb.fsmi.helper.*;
import de.upb.fsmi.receivers.*;

@EFragment(R.layout.empty_view)
public class StatusViewFragment extends Fragment implements
		UpdateCompletedListener {

	private static final String TAG = StatusViewFragment.class.getSimpleName();

	private CardUI cardsView;

	@Bean
	DataKeeper dataKeeper;

	UpdateCompletedReceiver updateCompletedReceiver = new UpdateCompletedReceiver(
			this);

	private StatusCard statusCard;

	@Override
	public void onResume() {
		updateCompletedReceiver.registerReceiver(getActivity()
				.getApplicationContext());
		cardsView = (CardUI) getActivity().findViewById(R.id.cardsview);
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

		statusCard = new StatusCard(dataKeeper.getFsmiState());

		cardsView.addCard(statusCard);

		refreshDisplayedData();
	}

	@UiThread
	protected void refreshDisplayedData() {
		refreshStatusCard();
		cardsView.refresh();
	}

	void refreshStatusCard() {
		statusCard.setStatus(dataKeeper.getFsmiState());
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
