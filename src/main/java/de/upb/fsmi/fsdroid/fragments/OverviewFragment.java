package de.upb.fsmi.fsdroid.fragments;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import de.upb.fsmi.fsdroid.R;
import de.upb.fsmi.fsdroid.cards.views.MeetingCardViewGroup;
import de.upb.fsmi.fsdroid.cards.views.StatusCardViewGroup;
import de.upb.fsmi.fsdroid.sync.AccountCreator;
import de.upb.fsmi.fsdroid.sync.SyncAdapter;

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

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @OptionsItem(R.id.ab_refresh)
    void refresh() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        settingsBundle.putInt(SyncAdapter.SyncTypes.KEY, SyncAdapter.SyncTypes.STATUS);
        ContentResolver.requestSync(mAccountCreator.getAccountRegisterAccount(), AccountCreator.getAuthority(), settingsBundle);

    }

}
