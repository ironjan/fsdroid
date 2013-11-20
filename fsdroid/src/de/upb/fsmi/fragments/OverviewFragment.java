package de.upb.fsmi.fragments;

import java.util.*;

import org.slf4j.*;

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
import de.upb.fsmi.receivers.*;

@EFragment(R.layout.fragment_overview)
@OptionsMenu(R.menu.menu_main)
public class OverviewFragment extends Fragment implements
		UpdateCompletedListener {

	@StringRes
	String misc;

	private static final String TAG = OverviewFragment.class.getSimpleName();

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	UpdateCompletedReceiver updateCompletedReceiver = new UpdateCompletedReceiver(
			this);

	private MenuItem ab_refresh;

	private boolean mProgressShown;

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
	public void onResume() {
		updateCompletedReceiver.registerReceiver(getActivity()
				.getApplicationContext());
		super.onResume();
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		ab_refresh = menu.findItem(R.id.ab_refresh);
		displayProgressBar(mProgressShown);
	}

	@Override
	public void onPause() {
		updateCompletedReceiver.unregisterReceiver();
		super.onPause();
	}

	@AfterViews
	@UiThread
	protected void initCardView() {
		LOGGER.debug("initializing card views");

		cardsView.setSwipeable(false);

		meetingCard = new MeetingCard(null);
		statusCard = new StatusCard(dataKeeper.getFsmiState());
		LOGGER.trace("Created cards");

		cardsView.addCard(statusCard);
		cardsView.addCard(meetingCard);
		LOGGER.trace("Added cards to cardsview.");

		LOGGER.debug("Card views are initialized");
		refreshData();
	}

	@Background
	void refreshData() {
		try {
			dataKeeper.refresh(false);
		} catch (NoAvailableNetworkException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		updateCompleted();
	}

	@UiThread
	protected void refreshCards() {
		refreshStatusCard();
		refreshMeetingCard();
		cardsView.refresh();
		displayProgressBar(false);
	}

	void refreshStatusCard() {
		statusCard.setStatus(dataKeeper.getFsmiState());
	}

	void refreshMeetingCard() {
		Date nextMeetingDate = dataKeeper.getNextMeetingDate();
		if (nextMeetingDate != null) {
			meetingCard.setDate(dataKeeper.getNextMeetingDate());
		}
	}

	void logError(final Exception e) {
		Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
		Log.e(TAG, e.getMessage(), e);
	}

	@OptionsItem(R.id.ab_refresh)
	void refresh() {
		displayProgressBar(true);
		refreshData();
	}

	@UiThread
	void displayProgressBar(final boolean visible) {
		mProgressShown = visible;
		Log.v(TAG, "ProgressBar shown=" + visible);
		if (null != ab_refresh) {
			ab_refresh.setVisible(!visible);
		}
		ActionBarActivity activity = (ActionBarActivity) getActivity();
		activity.setSupportProgressBarIndeterminateVisibility(visible);
	}

	@Override
	public void updateCompleted() {
		if (dataKeeper.isRefreshing()) {
			return;
		}
		refreshCards();
		displayProgressBar(false);
	}
}
