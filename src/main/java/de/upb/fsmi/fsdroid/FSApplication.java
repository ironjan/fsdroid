package de.upb.fsmi.fsdroid;

import android.app.*;
import android.util.*;

import org.androidannotations.annotations.*;

import de.upb.fsmi.fsdroid.db.*;

@EApplication
public class FSApplication extends Application {
    private final static String TAG = FSApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate()");
        DatabaseManager.getInstance(getApplicationContext());
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate()");
    }

}
