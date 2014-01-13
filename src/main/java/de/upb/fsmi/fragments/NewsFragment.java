package de.upb.fsmi.fragments;

import android.accounts.Account;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import de.upb.fsmi.BuildConfig;
import de.upb.fsmi.R;
import de.upb.fsmi.cards.NewsCard;
import de.upb.fsmi.cards.NewsCard_;
import de.upb.fsmi.db.DatabaseManager;
import de.upb.fsmi.helper.DataKeeper;
import de.upb.fsmi.helper.MeetingPrefs_;
import de.upb.fsmi.helper.UpdateCompletedListener;
import de.upb.fsmi.news.persistence.NewsItem;
import de.upb.fsmi.receivers.UpdateCompletedReceiver;
import de.upb.fsmi.rest.RestBean;
import de.upb.fsmi.sync.AccountCreator;

@EFragment(R.layout.fragment_news)
@OptionsMenu(R.menu.menu_main)
public class NewsFragment extends Fragment implements UpdateCompletedListener {

    public static final String AUTHORITY = AccountCreator.getAuthority();
    private static final String TAG = NewsFragment.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(TAG);
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
    Account mAccount;
    @Bean
    AccountCreator mAccountCreator;
    @StringRes
    String news;
    private NewsCard dummyNewsCard;
    private DatabaseManager databaseManager;
    private MenuItem ab_refresh;
    private boolean mProgressShown;

    @Override
    public void onCreate(Bundle pSavedInstanceState) {
        if (BuildConfig.DEBUG) LOGGER.debug("onCreate({})", pSavedInstanceState);

        super.onCreate(pSavedInstanceState);
        databaseManager = DatabaseManager.getInstance();

        if (BuildConfig.DEBUG) LOGGER.debug("onCreate({}) done", pSavedInstanceState);
    }

    @Override
    public void onResume() {
        if (BuildConfig.DEBUG) LOGGER.debug("onResume()");

        updateCompletedReceiver.registerReceiver(getActivity()
                .getApplicationContext());
        super.onResume();
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setTitle(news);

        if (BuildConfig.DEBUG) LOGGER.debug("onResume() done");
    }

    @Override
    public void onPause() {
        if (BuildConfig.DEBUG) LOGGER.debug("onPause()");

        updateCompletedReceiver.unregisterReceiver();
        super.onPause();

        if (BuildConfig.DEBUG) LOGGER.debug("onPause() done");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (BuildConfig.DEBUG) LOGGER.debug("onCreateOptionsMenu({},{})", menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
        ab_refresh = menu.findItem(R.id.ab_refresh);
        displayProgressBar(mProgressShown);

        if (BuildConfig.DEBUG) LOGGER.debug("onCreateOptionsMenu({},{}) done", menu, inflater);
    }

    @AfterViews
    @UiThread
    protected void initCardView() {
        if (BuildConfig.DEBUG) LOGGER.debug("initCardView()");

        cardsview.setSwipeable(false);

        dummyNewsCard = NewsCard_.getInstance_(getActivity());
        cardsview.addCard(dummyNewsCard);
        cardsview.refresh();

        displayProgressBar(true);
        displayKnownNews();

        if (BuildConfig.DEBUG) LOGGER.debug("initCardView() done");
    }

    @UiThread
    void displayKnownNews() {
        if (BuildConfig.DEBUG) LOGGER.debug("displayKnownNews()");

        List<NewsItem> allNewsItems = databaseManager.getAllNewsItems();
        Log.v(TAG, "Displayed " + allNewsItems.size() + " news items");
        showNews(allNewsItems);
        displayProgressBar(false);

        if (BuildConfig.DEBUG) LOGGER.debug("displayKnownNews() done");
    }

    @OptionsItem(R.id.ab_refresh)
    @Background
    @Trace
    void refreshNews() {
        if (BuildConfig.DEBUG) LOGGER.debug("refreshNews()");

        displayProgressBar(true);
        Log.v(TAG, "Refreshing news");
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);

        while (ContentResolver.isSyncActive(mAccount, AUTHORITY)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        Log.v(TAG, "Refresh complete.");
        displayKnownNews();

        if (BuildConfig.DEBUG) LOGGER.debug("refreshNews() done");
    }


    @UiThread
    void displayProgressBar(boolean visible) {
        if (BuildConfig.DEBUG) LOGGER.debug("displayProgressBar({})", visible);

        mProgressShown = visible;
        Log.v(TAG, "ProgressBar shown=" + visible);
        if (null != ab_refresh) {
            ab_refresh.setVisible(!visible);
        }
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.setSupportProgressBarIndeterminateVisibility(visible);

        if (BuildConfig.DEBUG) LOGGER.debug("displayProgressBar({})", visible);
    }

    @UiThread
    void showNews(List<NewsItem> pNewsItems) {
        if (BuildConfig.DEBUG) LOGGER.debug("showNews({})", pNewsItems);

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

        if (BuildConfig.DEBUG) LOGGER.debug("showNews({}) done", pNewsItems);
    }

    @Override
    @UiThread
    public void updateCompleted() {
        if (BuildConfig.DEBUG) LOGGER.debug("updateCompleted()");

        cardsview.refresh();

        if (BuildConfig.DEBUG) LOGGER.debug("updateCompleted() done");
    }

}
