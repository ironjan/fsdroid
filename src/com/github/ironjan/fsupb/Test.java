package com.github.ironjan.fsupb;

import android.util.*;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;
import com.github.ironjan.fsupb.model.*;
import com.github.ironjan.fsupb.receiver.*;
import com.googlecode.androidannotations.annotations.*;
import com.manuelpeinado.refreshactionitem.*;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;

import de.keyboardsurfer.android.widget.crouton.*;

@EActivity(R.layout.activity_test)
public class Test extends SherlockFragmentActivity implements
		RefreshActionListener, UpdateCompletedListener, UpdateStartedListener {

	private static final String ACTION_DATA_REFRESH_COMPLETED = DataKeeper.ACTION_DATA_REFRESH_COMPLETED;
	private static final String ACTION_DATA_REFRESH_STARTED = DataKeeper.ACTION_DATA_REFRESH_STARTED;
	protected static final String TAG = Test.class.getSimpleName();

	RefreshActionItem mRefreshActionItem;

	@Bean
	DataKeeper dataKeeper;

	private final UpdateCompletedReceiver updateCompletedReceiver = new UpdateCompletedReceiver(
			this);
	private final CustomBroadcastReceiver updateStartedReceiver = new UpdateStartedReceiver(
			this);

	@Override
	public void updateStarted() {
		showProgress(true);
		Log.v(TAG, ACTION_DATA_REFRESH_STARTED);
	}

	@Override
	public void updateCompleted() {
		showProgress(false);
		Log.v(TAG, ACTION_DATA_REFRESH_COMPLETED);
	}

	@UiThread
	void showCrouton(final String msg) {
		Crouton.showText(this, msg, Style.ALERT);
	}

	void showProgress(boolean progressShown) {
		if (mRefreshActionItem != null) {
			mRefreshActionItem.showProgress(progressShown);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_main, menu);
		MenuItem item = menu.findItem(R.id.ab_refresh);
		mRefreshActionItem = (RefreshActionItem) item.getActionView();
		mRefreshActionItem.setMenuItem(item);
		mRefreshActionItem
				.setProgressIndicatorType(ProgressIndicatorType.INDETERMINATE);
		mRefreshActionItem.setRefreshActionListener(this);
		// mRefreshActionItem.showProgress(true);

		return true;
	}

	@Override
	protected void onResume() {
		updateCompletedReceiver.registerReceiver(getApplicationContext());
		updateStartedReceiver.registerReceiver(getApplicationContext());
		super.onResume();
	}

	@Override
	protected void onPause() {
		updateCompletedReceiver.unregisterReceiver();
		updateStartedReceiver.unregisterReceiver();
		super.onPause();
	}

	@Override
	public void onRefreshButtonClick(RefreshActionItem sender) {
		sender.showProgress(true);
		try {
			dataKeeper.refresh(true);
		} catch (NoAvailableNetworkException e) {
			sender.showProgress(false);
			showCrouton("Network unavailable.");
		}

	}
}
