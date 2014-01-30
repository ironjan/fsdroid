package de.upb.fsmi.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.fima.cardsui.views.CardUI;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Trace;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import de.upb.fsmi.R;
import de.upb.fsmi.cards.MeetingCard;
import de.upb.fsmi.cards.StatusCard;
import de.upb.fsmi.helper.DataKeeper;
import de.upb.fsmi.helper.NoAvailableNetworkException;

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

	@AfterViews
	@UiThread
    @Trace
    protected void initCardView() {
		LOGGER.debug("initializing card views");
		
		cardsView.setSwipeable(false);

        createCards();
    }

    @Background
    @Trace
    void createCards() {
        meetingCard = new MeetingCard(null);
        statusCard = new StatusCard(dataKeeper.getFsmiState());

        addCards();

        refreshData();
    }

    @UiThread
    @Trace
    void addCards() {
        cardsView.addCard(statusCard);
        cardsView.addCard(meetingCard);

        LOGGER.trace("Added cards to cardsview.");
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
