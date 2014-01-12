package de.upb.fsmi.sync;

import android.accounts.*;
import android.annotation.*;
import android.content.*;
import android.os.*;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.*;

import org.slf4j.*;

import java.util.*;

import de.upb.fsmi.*;
import de.upb.fsmi.db.*;
import de.upb.fsmi.news.persistence.*;
import de.upb.fsmi.rest.*;


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
        if (BuildConfig.DEBUG)
            LOGGER.debug("Created SyncAdapter({},{},{})", new Object[]{context, autoInitialize, allowParallelSyncs});
    }


    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        if (BuildConfig.DEBUG)
            LOGGER.debug("onPerformeSync({},{},{},{},{})", new Object[]{account, bundle, s, contentProviderClient, syncResult});

        try {
            Channel news = mRss.getNews();
            persist(news);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        if (BuildConfig.DEBUG)
            LOGGER.debug("onPerformeSync({},{},{},{},{}) done", new Object[]{account, bundle, s, contentProviderClient, syncResult});
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
