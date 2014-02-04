package de.upb.fsmi.fsdroid.receivers;

import android.content.*;
import android.util.*;

import de.upb.fsmi.fsdroid.helper.*;

public class UpdateCompletedReceiver extends BroadcastReceiver implements
        CustomBroadcastReceiver {

    private static final String TAG = UpdateCompletedReceiver.class
            .getSimpleName();

    private final IntentFilter filter = new IntentFilter(
            DataKeeper.ACTION_DATA_REFRESH_COMPLETED);

    private final UpdateCompletedListener listener;

    private Context context;

    public UpdateCompletedReceiver(UpdateCompletedListener listener) {
        super();
        this.listener = listener;
        Log.d(TAG, TAG + " created with listener: "
                + listener.getClass().getSimpleName());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received intent(action=" + intent.getAction()
                + "; called listener:" + listener);
        listener.updateCompleted();
    }

    @Override
    public void registerReceiver(Context context) {
        this.context = context;
        context.registerReceiver(this, filter);
        Log.d(TAG, "Registered " + TAG + " @"
                + context.getClass().getSimpleName() + " with listener:"
                + listener.getClass().getSimpleName());
    }

    @Override
    public void unregisterReceiver() {
        if (context == null) {
            return;
        }
        try {
            context.unregisterReceiver(this);
            Log.d(TAG, "Unregistered " + TAG + " @"
                    + context.getClass().getSimpleName());
        } catch (IllegalArgumentException e) {
            // nothing to do
        }
    }

}
