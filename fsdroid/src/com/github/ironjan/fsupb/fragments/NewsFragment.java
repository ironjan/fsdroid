package com.github.ironjan.fsupb.fragments;

import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.github.ironjan.fsupb.R;
import com.github.ironjan.fsupb.cards.MeetingCard;
import com.github.ironjan.fsupb.cards.StatusCard;
import com.github.ironjan.fsupb.helper.DataKeeper;
import com.github.ironjan.fsupb.helper.NoAvailableNetworkException;
import com.github.ironjan.fsupb.helper.UpdateCompletedListener;
import com.github.ironjan.fsupb.receiver.UpdateCompletedReceiver;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.manuelpeinado.refreshactionitem.ProgressIndicatorType;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@EFragment(R.layout.fragment_news)
@OptionsMenu(R.menu.menu_main)
public class NewsFragment extends SherlockFragment implements
		UpdateCompletedListener, RefreshActionListener {

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

	private RefreshActionItem mRefreshActionItem;

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
		MenuItem item = menu.findItem(R.id.ab_refresh);
		mRefreshActionItem = (RefreshActionItem) item.getActionView();
		mRefreshActionItem.setMenuItem(item);
		mRefreshActionItem
				.setProgressIndicatorType(ProgressIndicatorType.INDETERMINATE);
		mRefreshActionItem.setRefreshActionListener(this);
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

	@UiThread
	void showProgress(RefreshActionItem sender, boolean shown) {
		sender.showProgress(shown);
	}

	@Background
	@Override
	public void onRefreshButtonClick(RefreshActionItem sender) {
		showProgress(sender, true);
		try {
			dataKeeper.refresh(true);
		} catch (NoAvailableNetworkException e) {
			Crouton.showText(getActivity(), "Network unavailable.", Style.INFO);
		}
		showProgress(sender, false);

		refreshDisplayedData();
	}

}
