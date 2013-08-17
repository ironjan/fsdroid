package de.upb.fsmi.receivers;

import android.content.Context;

public interface CustomBroadcastReceiver {

	public abstract void registerReceiver(Context context);

	public abstract void unregisterReceiver();

}