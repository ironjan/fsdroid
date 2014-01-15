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
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.*;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.*;
import com.google.code.rome.android.repackaged.com.sun.syndication.fetcher.impl.*;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.*;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(TAG);


    public static final String SYNC_FINISHED = "SYNC_FINISHED";
    private static SyncAdapter instance = null;
    private final Context mContext;


    private SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        if (BuildConfig.DEBUG) LOGGER.debug("Created SyncAdapter({},{})", context, autoInitialize);
        this.mContext = context;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        this.mContext = context;
        if (BuildConfig.DEBUG)
            LOGGER.debug("Created SyncAdapter({},{},{})", new Object[]{context, autoInitialize, allowParallelSyncs});
    }

    public static SyncAdapter getInstance(Context context){
        if(instance == null){
            instance = createSyncAdapterSingleton(context);
        }

        return instance;
    }

    private static SyncAdapter createSyncAdapterSingleton(Context context) {
        Context applicationContext = context.getApplicationContext();

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB){
            return new SyncAdapter(applicationContext, true,false);
        }else{
           return new SyncAdapter(applicationContext, true);
        }
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
        else if (BuildConfig.DEBUG) LOGGER.debug("executeSync({})", force);

        try {

            RestBean_ mRss = RestBean_.getInstance_(mContext);

            LOGGER.debug("{}", mRss);

            Channel news = mRss.getNews();

            LOGGER.debug("Got news.");
            persist(news);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        broadcastSyncEnd();

        if (force)
            LOGGER.warn("executeSync({}) done", force);
        else if (BuildConfig.DEBUG) LOGGER.debug("executeSync({})", force);
    }

    private void persist(Channel news) {
        if(BuildConfig.DEBUG) LOGGER.debug("persist({})", (news != null)?"news":"null");

        if (null == news) {
            return;
        }

        @SuppressWarnings("unchecked")
        List<Item> items = news.getItems();
        DatabaseManager databaseManager = DatabaseManager.getInstance();

        for (Item item : items) {
            databaseManager.createOrUpdate(new NewsItem(item));
        }

        if(BuildConfig.DEBUG) LOGGER.debug("persist({}) done", (news != null)?"news":"null");
    }

    private void broadcastSyncEnd() {
        if (BuildConfig.DEBUG) LOGGER.debug("broadcastSyncEnd()");

        Intent i = new Intent(SYNC_FINISHED);
        getContext().sendBroadcast(i);
        if (BuildConfig.DEBUG) LOGGER.debug("broadcastSyncEnd() done");
    }
}
