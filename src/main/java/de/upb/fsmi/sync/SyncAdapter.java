package de.upb.fsmi.sync;

import android.accounts.*;
import android.annotation.*;
import android.content.*;
import android.os.*;

import org.slf4j.*;

import de.upb.fsmi.*;


/**
 * SyncAdapter to implement the synchronization.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = SyncAdapter.class.getSimpleName();
    Logger LOGGER = LoggerFactory.getLogger(TAG);


    public static final String SYNC_FINISHED = "SYNC_FINISHED";


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
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

        // TODO implement
        for (int i = 0; i < 20; i++) {
            LOGGER.error("Synching stub...");
        }

        if (BuildConfig.DEBUG)
            LOGGER.debug("onPerformeSync({},{},{},{},{}) done", new Object[]{account, bundle, s, contentProviderClient, syncResult});
    }

    private void broadcastSyncEnd() {
        if (BuildConfig.DEBUG) LOGGER.debug("broadcastSyncEnd()");

        Intent i = new Intent(SYNC_FINISHED);
        getContext().sendBroadcast(i);
        if (BuildConfig.DEBUG) LOGGER.debug("broadcastSyncEnd() done");
    }
}
