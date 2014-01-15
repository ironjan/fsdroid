package de.upb.fsmi.sync;

import android.app.*;
import android.content.*;
import android.os.*;

import org.slf4j.*;

import de.upb.fsmi.*;

public class SyncService extends Service {
    private static final String TAG = SyncService.class.getSimpleName();
    Logger LOGGER = LoggerFactory.getLogger(TAG);

    private static SyncAdapter sSyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();

    public SyncService() {
        if (BuildConfig.DEBUG) LOGGER.debug("Created SyncService.");
    }

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) LOGGER.debug("onCreate()");

        super.onCreate();
        synchronized (sSyncAdapterLock) {
            if(sSyncAdapter == null){
                sSyncAdapter = SyncAdapter.getInstance(getApplicationContext());
            }
        }

        if (BuildConfig.DEBUG) LOGGER.debug("onCreate() done");
    }



    @Override
    public IBinder onBind(Intent intent) {
        if (BuildConfig.DEBUG) LOGGER.debug("onBind({})", intent);

        return sSyncAdapter.getSyncAdapterBinder();
    }

}
