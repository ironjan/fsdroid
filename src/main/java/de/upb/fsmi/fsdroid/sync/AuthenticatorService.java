package de.upb.fsmi.fsdroid.sync;

import android.app.*;
import android.content.*;
import android.os.*;

import org.slf4j.*;

import de.upb.fsmi.fsdroid.*;

/**
 * A service that instantiates the {@link de.upb.fsmi.fsdroid.sync.StubAuthenticator}
 */
public class AuthenticatorService extends Service {
    private static final String TAG = AuthenticatorService.class.getSimpleName();
    Logger LOGGER = LoggerFactory.getLogger(TAG);

    private StubAuthenticator mAuthenticator;

    public AuthenticatorService() {
        if (BuildConfig.DEBUG) LOGGER.debug("Created AuthenticatorService.");
    }

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) LOGGER.debug("onCreate()");

        super.onCreate();
        mAuthenticator = new StubAuthenticator(this);

        if (BuildConfig.DEBUG) LOGGER.debug("onCreate() done");
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (BuildConfig.DEBUG) LOGGER.debug("onBind({}) done", intent);

        return mAuthenticator.getIBinder();
    }
}
