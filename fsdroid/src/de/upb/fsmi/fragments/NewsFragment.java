package de.upb.fsmi.fragments;

import java.util.*;

import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;

import com.fima.cardsui.views.*;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.*;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.*;
import com.googlecode.androidannotations.annotations.sharedpreferences.*;

import de.upb.fsmi.*;
import de.upb.fsmi.cards.*;
import de.upb.fsmi.db.*;
import de.upb.fsmi.helper.*;
import de.upb.fsmi.news.persistence.*;
import de.upb.fsmi.receivers.*;
import de.upb.fsmi.rest.*;

@EFragment(R.layout.fragment_news)
@OptionsMenu(R.menu.menu_main)
public class NewsFragment extends Fragment implements UpdateCompletedListener {

	private static final String TAG = NewsFragment.class.getSimpleName();

	@ViewById
	CardUI cardsview;

	@Bean
	DataKeeper dataKeeper;

	@Bean
	RestBean mRss;

	@Pref
	MeetingPrefs_ mPrefs;

	UpdateCompletedReceiver updateCompletedReceiver = new UpdateCompletedReceiver(
			this);

	private NewsCard dummyNewsCard;

	private DatabaseManager databaseManager;

	private MenuItem ab_refresh;

	private boolean mProgressShown;

	@StringRes
	String news;

	@Override
	public void onCreate(final Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		databaseManager = DatabaseManager.getInstance();
	}

	@Override
	public void onResume() {
		updateCompletedReceiver.registerReceiver(getActivity()
				.getApplicationContext());
		super.onResume();
		((ActionBarActivity) getActivity()).getSupportActionBar()
				.setTitle(news);
	}

	@Override
	public void onPause() {
		updateCompletedReceiver.unregisterReceiver();
		super.onPause();
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
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
		long currentTimeMillis = System.currentTimeMillis();

		displayProgressBar(true);
		Log.v(TAG, "Refreshing news");
		Channel news = mRss.getNews();
		persist(news);
		Log.v(TAG, "Refresh complete.");
		displayKnownNews();
	}

	private void persist(final Channel news) {
		if (null == news) {
			return;
		}

		@SuppressWarnings("unchecked")
		List<Item> items = news.getItems();

		for (Item item : items) {
			databaseManager.createOrUpdate(new NewsItem(item));
		}
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

	@UiThread
	void showNews(final List<NewsItem> pNewsItems) {
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
		if (!dataKeeper.isRefreshing()) {
			cardsview.refresh();
		}
	}

}
