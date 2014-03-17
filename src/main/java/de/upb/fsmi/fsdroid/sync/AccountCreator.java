package de.upb.fsmi.fsdroid.sync;

import android.accounts.*;
import android.content.*;
import android.util.*;

import org.androidannotations.annotations.*;
import org.slf4j.*;

import de.upb.fsmi.fsdroid.*;

/**
 * Created by ljan on 10.01.14.
 */
@EBean(scope = EBean.Scope.Singleton)
public class AccountCreator {
    private static final String TAG = AccountCreator.class.getSimpleName();
    Logger LOGGER = LoggerFactory.getLogger(TAG);

    @RootContext
    Context mContext;
    @SystemService
    AccountManager mAccountManager;

    public AccountCreator() {
    }

    /**
     * Neded for synchroniztation initialization
     */
    static final String AUTHORITY = BuildConfig.AUTHORITY,
            ACCOUNT_TYPE = BuildConfig.ACCOUNT_TYPE,
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

    public static String getAccountName() {
        return ACCOUNT;
    }


    public static String getAuthority() {
        return AUTHORITY;
    }
}
