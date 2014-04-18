package de.upb.fsmi.fsdroid.fragments;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.res.*;

import de.upb.fsmi.fsdroid.*;
import de.upb.fsmi.fsdroid.cards.views.*;
import de.upb.fsmi.fsdroid.sync.*;

@EFragment(R.layout.fragment_overview)
@OptionsMenu(R.menu.menu_main)
public class OverviewFragment extends Fragment {

    @StringRes
    String misc;

    @ViewById
    StatusCardViewGroup statusCard;
    @ViewById
    MeetingCardViewGroup meetingCard;

    @AfterViews
    void updateTitle() {
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setTitle(misc);
    }

    @Bean
    AccountCreator mAccountCreator;

    @OptionsItem(R.id.ab_refresh)
    void refresh() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(mAccountCreator.getAccountRegisterAccount(), AccountCreator.getAuthority(), settingsBundle);

    }

}
