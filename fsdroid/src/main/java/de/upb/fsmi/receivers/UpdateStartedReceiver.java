package de.upb.fsmi.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import de.upb.fsmi.helper.DataKeeper;
import de.upb.fsmi.helper.UpdateStartedListener;

public class UpdateStartedReceiver extends BroadcastReceiver implements
		CustomBroadcastReceiver {

	private static final String TAG = UpdateStartedReceiver.class
			.getSimpleName();

	private final IntentFilter filter = new IntentFilter(
			DataKeeper.ACTION_DATA_REFRESH_STARTED);

	private final UpdateStartedListener listener;

	private Context context;

	public UpdateStartedReceiver(UpdateStartedListener listener) {
		super();
		this.listener = listener;
		Log.d(TAG, TAG + " created with listener: "
				+ listener.getClass().getSimpleName());
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Received intent(action=" + intent.getAction()
				+ "; called listener:" + listener);
		listener.updateStarted();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.ironjan.fsupb.CustomBroadcastReceiver#registerReceiver()
	 */
	@Override
	public void registerReceiver(Context context) {
		this.context = context;
		context.registerReceiver(this, filter);
		Log.d(TAG, "Registered " + TAG + " @"
				+ context.getClass().getSimpleName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.ironjan.fsupb.CustomBroadcastReceiver#unregisterReceiver()
	 */
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
