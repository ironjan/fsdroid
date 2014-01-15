package de.upb.fsmi.fragments;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;

import com.fima.cardsui.views.*;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.Trace;
import org.androidannotations.annotations.res.*;
import org.androidannotations.annotations.sharedpreferences.*;
import org.slf4j.*;

import java.util.*;

import de.upb.fsmi.*;
import de.upb.fsmi.cards.*;
import de.upb.fsmi.db.*;
import de.upb.fsmi.helper.*;
import de.upb.fsmi.news.persistence.*;
import de.upb.fsmi.receivers.*;
import de.upb.fsmi.sync.*;

@EFragment(R.layout.fragment_news)
@OptionsMenu(R.menu.menu_main)
public class NewsFragment extends Fragment implements UpdateCompletedListener, SyncStatusObserver {

    private static final String TAG = NewsFragment.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(TAG);
    @ViewById
    CardUI cardsview;
    @Bean
    DataKeeper dataKeeper;

    @Pref
    MeetingPrefs_ mPrefs;
    UpdateCompletedReceiver updateCompletedReceiver = new UpdateCompletedReceiver(
            this);
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

        refreshNews();

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

        displayKnownNews();
        displayProgressBar(true);

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
        ContentResolver.addStatusChangeListener(ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE | ContentResolver.SYNC_OBSERVER_TYPE_PENDING, this);

        ContentResolver.requestSync(mAccountCreator.getAccountRegisterAccount(), AccountCreator.getAuthority(), settingsBundle);

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
        if (BuildConfig.DEBUG) LOGGER.debug("showNews(...)");

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

        if (BuildConfig.DEBUG) LOGGER.debug("showNews(...) done");
    }

    @Override
    @UiThread
    public void updateCompleted() {
        if (BuildConfig.DEBUG) LOGGER.debug("updateCompleted()");

        cardsview.refresh();

        if (BuildConfig.DEBUG) LOGGER.debug("updateCompleted() done");
    }

    @Override
    public void onStatusChanged(int status) {
        if (ContentResolver.isSyncActive(mAccountCreator.getAccountRegisterAccount(), mAccountCreator.getAuthority())) {
            return;
        }

        displayKnownNews();
    }
}
