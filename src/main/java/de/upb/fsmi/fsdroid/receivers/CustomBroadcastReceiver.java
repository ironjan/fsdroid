package de.upb.fsmi.fsdroid.receivers;

import android.content.*;

public interface CustomBroadcastReceiver {

    public abstract void registerReceiver(Context context);

    public abstract void unregisterReceiver();

}