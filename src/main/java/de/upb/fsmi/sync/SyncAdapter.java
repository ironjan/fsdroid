package de.upb.fsmi.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Channel;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import de.upb.fsmi.BuildConfig;
import de.upb.fsmi.db.DatabaseManager;
import de.upb.fsmi.news.persistence.NewsItem;
import de.upb.fsmi.rest.RestBean;
import de.upb.fsmi.rest.RestBean_;


/**
 * SyncAdapter to implement the synchronization.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = SyncAdapter.class.getSimpleName();
    Logger LOGGER = LoggerFactory.getLogger(TAG);

    RestBean mRss;

    private DatabaseManager databaseManager;

    public static final String SYNC_FINISHED = "SYNC_FINISHED";


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        databaseManager = DatabaseManager.getInstance();
        mRss = RestBean_.getInstance_(context);
        if (BuildConfig.DEBUG) LOGGER.debug("Created SyncAdapter({},{})", context, autoInitialize);

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        databaseManager = DatabaseManager.getInstance();
        mRss = RestBean_.getInstance_(context);
        if (BuildConfig.DEBUG)
            LOGGER.debug("Created SyncAdapter({},{},{})", new Object[]{context, autoInitialize, allowParallelSyncs});
    }


    @Override
    public void onPerformSync(Account account, Bundle bundle, String authority, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        if (BuildConfig.DEBUG)
            LOGGER.debug("onPerformeSync({},{},{},{},{})", new Object[]{account, bundle, authority, contentProviderClient, syncResult});

        executeSync(false);

        if (BuildConfig.DEBUG)
            LOGGER.debug("onPerformeSync({},{},{},{},{}) done", new Object[]{account, bundle, authority, contentProviderClient, syncResult});
    }

    public void executeSync(boolean force) {
        if (force)
            LOGGER.warn("executeSync({})", force);

        try {
            Channel news = mRss.getNews();
            persist(news);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        broadcastSyncEnd();
        if (force)
            LOGGER.warn("executeSync({}) done", force);

    }

    private void persist(Channel news) {
        if (null == news) {
            return;
        }

        @SuppressWarnings("unchecked")
        List<Item> items = news.getItems();

        for (Item item : items) {
            databaseManager.createOrUpdate(new NewsItem(item));
        }
    }

    private void broadcastSyncEnd() {
        if (BuildConfig.DEBUG) LOGGER.debug("broadcastSyncEnd()");

        Intent i = new Intent(SYNC_FINISHED);
        getContext().sendBroadcast(i);
        if (BuildConfig.DEBUG) LOGGER.debug("broadcastSyncEnd() done");
    }
}
