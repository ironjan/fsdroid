package de.upb.fsmi.fragments;

import java.util.Iterator;
import java.util.List;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.fima.cardsui.views.CardUI;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Channel;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.rest.RestService;

import de.upb.fsmi.R;
import de.upb.fsmi.cards.DummyNewsCard;
import de.upb.fsmi.cards.MeetingCard;
import de.upb.fsmi.cards.StatusCard;
import de.upb.fsmi.helper.DataKeeper;
import de.upb.fsmi.helper.UpdateCompletedListener;
import de.upb.fsmi.news.RSSInterface;
import de.upb.fsmi.receivers.UpdateCompletedReceiver;

@EFragment(R.layout.fragment_news)
@OptionsMenu(R.menu.menu_main)
public class NewsFragment extends Fragment implements UpdateCompletedListener {

	private static final String TAG = NewsFragment.class.getSimpleName();

	@ViewById
	CardUI cardsview;

	@Bean
	DataKeeper dataKeeper;

	UpdateCompletedReceiver updateCompletedReceiver = new UpdateCompletedReceiver(this);

	private StatusCard statusCard;
	private MeetingCard meetingCard;
	private DummyNewsCard dummyNewsCard;

	private boolean statusCardHidden = false, meetingCardHidden = false;

	@RestService
	RSSInterface rssInterface;
	@Background
	void testRSS() {
		Channel news = rssInterface.getNews();
		List<Item> items = news.getItems();

		for (Iterator<Item> iterator = items.iterator(); iterator.hasNext();) {
			Item object = (Item) iterator.next();
			Log.v(TAG, object.getTitle());
		}
	}
	
	public void setStatusCardHidden(boolean statusCardHidden) {
		this.statusCardHidden = statusCardHidden;
	}

	public void setMeetingCardHidden(boolean meetingCardHidden) {
		this.meetingCardHidden = meetingCardHidden;
	}

	@Override
	public void onResume() {
		testRSS();
		updateCompletedReceiver.registerReceiver(getActivity().getApplicationContext());
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

		statusCard = new StatusCard(dataKeeper.getFsmiState());
		meetingCard = new MeetingCard(dataKeeper.getNextMeetingDate());
		dummyNewsCard = new DummyNewsCard();

		cardsview.addCard(statusCard);
		cardsview.addCard(meetingCard);
		cardsview.addCard(dummyNewsCard);

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
