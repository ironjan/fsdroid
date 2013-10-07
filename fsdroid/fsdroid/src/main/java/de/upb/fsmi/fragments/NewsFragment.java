package de.upb.fsmi.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;

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
import de.upb.fsmi.cards.NewsCard;
import de.upb.fsmi.db.DatabaseManager;
import de.upb.fsmi.helper.DataKeeper;
import de.upb.fsmi.helper.UpdateCompletedListener;
import de.upb.fsmi.news.RssRest;
import de.upb.fsmi.news.persistence.NewsItem;
import de.upb.fsmi.receivers.UpdateCompletedReceiver;

@EFragment(R.layout.fragment_news)
@OptionsMenu(R.menu.menu_main)
public class NewsFragment extends Fragment implements UpdateCompletedListener {

	private static final String TAG = NewsFragment.class.getSimpleName();

	@ViewById
	CardUI cardsview;

	@ViewById
	ProgressBar progressBar1;
	@Bean
	DataKeeper dataKeeper;

	@RestService
	RssRest mRss;

	UpdateCompletedReceiver updateCompletedReceiver = new UpdateCompletedReceiver(
			this);

	private NewsCard dummyNewsCard;

	private DatabaseManager databaseManager;

	@Override
	public void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		databaseManager = DatabaseManager.getInstance();
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
	}

	@AfterViews
	@UiThread
	protected void initCardView() {
		cardsview.setSwipeable(false);

		// dummyNewsCard = new NewsCard();
		//
		// cardsview.addCard(dummyNewsCard);

		displayKnownNews();
		
		refreshNews();
	}

	private void displayKnownNews() {
		List<NewsItem> allNewsItems = databaseManager.getAllNewsItems();
		Log.v(TAG, "Displayed " + allNewsItems.size() + " news items");
		showNews(allNewsItems);
	}

	@UiThread
	protected void refreshDisplayedData() {
		cardsview.refresh();
	}

	@Background
	void refreshNews() {
		Channel news = mRss.getNews();
		@SuppressWarnings("unchecked")
		List<Item> items = news.getItems();

		for (Item item : items) {
			databaseManager.createOrUpdate(new NewsItem(item));
		}
		displayKnownNews();
	}

	@UiThread
	void showNews(List<NewsItem> pNewsItems) {
		cardsview.clearCards();

		for (NewsItem item : pNewsItems) {
			NewsCard card = new NewsCard();
			card.bind(item);
			cardsview.addCard(card);
		}

		progressBar1.setVisibility(View.GONE);
		refreshDisplayedData();
	}

	@Override
	public void updateCompleted() {
		refreshDisplayedData();
	}

}
