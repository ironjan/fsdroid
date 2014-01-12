package de.upb.fsmi.fragments;

import java.util.*;

import org.slf4j.*;

import android.support.v4.app.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.fima.cardsui.views.*;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.res.*;
import com.j256.ormlite.stmt.query.*;

import de.upb.fsmi.*;
import de.upb.fsmi.cards.*;
import de.upb.fsmi.helper.*;

@EFragment(R.layout.fragment_overview)
@OptionsMenu(R.menu.menu_main)
public class OverviewFragment extends Fragment {

	@StringRes
	String misc;

	private static final String TAG = OverviewFragment.class.getSimpleName();
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

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
		refreshCards();
	}

	@UiThread
	protected void refreshCards() {
		refreshStatusCard();
		refreshMeetingCard();
		cardsView.refresh();
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

	void logError(Exception e) {
		Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
		Log.e(TAG, e.getMessage(), e);
	}

	@OptionsItem(R.id.ab_refresh)
	void refresh() {
		Toast.makeText(getActivity(), "Refresh started", Toast.LENGTH_SHORT)
				.show();
		refreshData();
	}

}
