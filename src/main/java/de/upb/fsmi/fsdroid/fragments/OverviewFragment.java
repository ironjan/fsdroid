package de.upb.fsmi.fsdroid.fragments;

import android.support.v4.app.*;
import android.support.v7.app.*;
import android.util.*;
import android.widget.*;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.res.*;
import org.slf4j.*;

import java.util.*;

import de.upb.fsmi.fsdroid.*;
import de.upb.fsmi.fsdroid.cards.views.*;
import de.upb.fsmi.fsdroid.helper.*;

@EFragment(R.layout.fragment_overview)
@OptionsMenu(R.menu.menu_main)
public class OverviewFragment extends Fragment {

    @StringRes
    String misc;

    private static final String TAG = OverviewFragment.class.getSimpleName();

    @ViewById
    StatusCardView statusCard;
    @ViewById
    MeetingCardView meetingCard;

    @AfterViews
    void updateTitle() {
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setTitle(misc);
    }

    @Bean
    DataKeeper dataKeeper;


    @AfterViews
    @UiThread
    @Trace
    protected void initCardView() {
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
    }

    void refreshStatusCard() {
        statusCard.bind(dataKeeper.getFsmiState());
    }

    void refreshMeetingCard() {
        Date nextMeetingDate = dataKeeper.getNextMeetingDate();
        if (nextMeetingDate != null) {
            meetingCard.bind(dataKeeper.getNextMeetingDate());
        }
    }


    @OptionsItem(R.id.ab_refresh)
    void refresh() {
        Toast.makeText(getActivity(), "Refresh started", Toast.LENGTH_SHORT)
                .show();
        refreshData();
    }

}
