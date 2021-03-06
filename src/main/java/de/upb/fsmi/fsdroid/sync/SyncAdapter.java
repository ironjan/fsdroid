package de.upb.fsmi.fsdroid.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import de.upb.fsmi.fsdroid.BuildConfig;
import de.upb.fsmi.fsdroid.sync.synchronizators.MeetingSynchronizator;
import de.upb.fsmi.fsdroid.sync.synchronizators.NewsSynchronizator;
import de.upb.fsmi.fsdroid.sync.synchronizators.StatusSynchronizator;


/**
 * SyncAdapter to implement the synchronization.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String SYNC_FINISHED = "SYNC_FINISHED";
    private static final String TAG = SyncAdapter.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(TAG);

    public static final class SyncTypes {
        public static final String KEY = "SYNC_TYPES";

        public static final int NEWS = 0x1;
        public static final int STATUS = 0x2;
        public static final int MEETING = 0x4;
        public static final int ALL = NEWS | STATUS | MEETING;
    }

    private static SyncAdapter instance = null;
    private static Object lock = new Object();
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

    public static SyncAdapter getInstance(Context context) {
        synchronized (lock) {
            if (instance == null) {
                instance = createSyncAdapterSingleton(context);
            }

            return instance;
        }
    }

    private static SyncAdapter createSyncAdapterSingleton(Context context) {
        Context applicationContext = context.getApplicationContext();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            return new SyncAdapter(applicationContext, true, false);
        } else {
            return new SyncAdapter(applicationContext, true);
        }
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String authority, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        if (BuildConfig.DEBUG)
            LOGGER.debug("onPerformSync({},{},{},{},{})", new Object[]{account, bundle, authority, contentProviderClient, syncResult});

        if (!bundle.containsKey(SyncTypes.KEY)) {
            bundle.putInt(SyncTypes.KEY, SyncTypes.ALL);
        }

        final int syncMask = bundle.getInt(SyncTypes.KEY);
        try {
            if ((syncMask & SyncTypes.STATUS) == SyncTypes.STATUS) {
                StatusSynchronizator.getInstance(mContext).executeSync();
            }
            if ((syncMask & SyncTypes.MEETING) == SyncTypes.MEETING) {
                MeetingSynchronizator.getInstance(mContext).executeSync();
            }
            if ((syncMask & SyncTypes.NEWS) == SyncTypes.NEWS) {
                NewsSynchronizator.getInstance(mContext).executeSync();
            }


        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (XmlPullParserException e) {
            LOGGER.error(e.getMessage(), e);
        }

        if (BuildConfig.DEBUG)
            LOGGER.debug("onPerformSync({},{},{},{},{}) done", new Object[]{account, bundle, authority, contentProviderClient, syncResult});
    }


}
