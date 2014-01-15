package de.upb.fsmi.sync;

import android.accounts.*;
import android.content.*;
import android.util.*;

import org.androidannotations.annotations.*;
import org.slf4j.*;

import de.upb.fsmi.*;

/**
 * Created by ljan on 10.01.14.
 */
@EBean
public class AccountCreator {
    private static final String TAG = AccountCreator.class.getSimpleName();
    Logger LOGGER = LoggerFactory.getLogger(TAG);

    @RootContext
    Context mContext;
    @SystemService
    AccountManager mAccountManager;

    /**
     * Neded for synchroniztation initialization
     */
    private static final String AUTHORITY = "de.ironjan.provider",
            ACCOUNT_TYPE = "fsdroid.ironjan.de",
            ACCOUNT = "dummy";

    private boolean mAccountCreated = false;
    private Account mAccount;


    public Account getAccountRegisterAccount() {
        if (BuildConfig.DEBUG) LOGGER.debug("getAccountRegisterAccount(...)");

        if (mAccount == null) {
            mAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
            mAccountCreated = mAccountManager.addAccountExplicitly(mAccount, null, null);

            if (mAccountCreated) {
                Log.i(TAG, "Synchronization account added.");
            } else {
                if (BuildConfig.DEBUG) LOGGER.debug("Account already existed.");
            }
        }

        if (BuildConfig.DEBUG) LOGGER.debug("getAccountRegisterAccount(...) done");
        return mAccount;
    }

    public boolean wasAccountCreated() {
        return mAccountCreated;
    }

    public static String getAuthority() {
        return AUTHORITY;
    }
}
