package de.upb.fsmi.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fima.cardsui.views.CardUI;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Channel;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.rest.RestService;

import de.upb.fsmi.R;
import de.upb.fsmi.cards.NewsCard;
import de.upb.fsmi.cards.NewsCard_;
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

	@Bean
	DataKeeper dataKeeper;

	@RestService
	RssRest mRss;

	UpdateCompletedReceiver updateCompletedReceiver = new UpdateCompletedReceiver(
			this);

	private NewsCard dummyNewsCard;

	private DatabaseManager databaseManager;

	private MenuItem ab_refresh;

	private boolean mProgressShown;

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
		ab_refresh = menu.findItem(R.id.ab_refresh);
		displayProgressBar(mProgressShown);
	}

	@AfterViews
	@UiThread
	protected void initCardView() {
		cardsview.setSwipeable(false);

		dummyNewsCard = NewsCard_.getInstance_(getActivity());
		cardsview.addCard(dummyNewsCard);
		cardsview.refresh();

		displayProgressBar(true);
		displayKnownNews();
		refreshNews();
	}

	@UiThread
	void displayKnownNews() {
		List<NewsItem> allNewsItems = databaseManager.getAllNewsItems();
		Log.v(TAG, "Displayed " + allNewsItems.size() + " news items");
		showNews(allNewsItems);
		displayProgressBar(false);
	}

	@OptionsItem(R.id.ab_refresh)
	@Background
	void refreshNews() {
		displayProgressBar(true);
		Log.v(TAG, "Refreshing news");
		Channel news = mRss.getNews();
		@SuppressWarnings("unchecked")
		List<Item> items = news.getItems();

		for (Item item : items) {
			databaseManager.createOrUpdate(new NewsItem(item));
		}
		Log.v(TAG, "Refresh complete.");
		displayKnownNews();
	}

	@UiThread
	void displayProgressBar(boolean visible) {
		mProgressShown = visible;
		Log.v(TAG, "ProgressBar shown=" + visible);
		if (null != ab_refresh) {
			ab_refresh.setVisible(!visible);
		}
		ActionBarActivity activity = (ActionBarActivity) getActivity();
		activity.setSupportProgressBarIndeterminateVisibility(visible);
	}

	@UiThread
	void showNews(List<NewsItem> pNewsItems) {
		if (pNewsItems.size() <= 0) {
			return;
		}

		cardsview.clearCards();

		for (NewsItem item : pNewsItems) {
			NewsCard card = new NewsCard();
			card.bind(item);
			cardsview.addCard(card);
		}

		cardsview.refresh();
	}

	@Override
	@UiThread
	public void updateCompleted() {
		cardsview.refresh();
	}

}
