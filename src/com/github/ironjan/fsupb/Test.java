package com.github.ironjan.fsupb;

import android.content.*;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;
import com.github.ironjan.fsupb.model.*;
import com.googlecode.androidannotations.annotations.*;
import com.manuelpeinado.refreshactionitem.*;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;

@EActivity(R.layout.activity_test)
public class Test extends SherlockFragmentActivity implements
		RefreshActionListener {

	private static final String ACTION_DATA_REFRESH_COMPLETED = DataKeeper.ACTION_DATA_REFRESH_COMPLETED;
	private static final String ACTION_DATA_REFRESH_STARTED = DataKeeper.ACTION_DATA_REFRESH_STARTED;

	RefreshActionItem mRefreshActionItem;

	@Bean
	DataKeeper dataKeeper;

	BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (ACTION_DATA_REFRESH_COMPLETED.equals(intent.getAction())) {
				showProgress(false);
			} else if (ACTION_DATA_REFRESH_STARTED.equals(intent.getAction())) {
				showProgress(true);
			}

		}

	};

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
		mRefreshActionItem.showProgress(true);

		return true;
	}

	@Override
	protected void onResume() {
		IntentFilter startedFilter = new IntentFilter(
				ACTION_DATA_REFRESH_STARTED);
		registerReceiver(br, startedFilter);

		IntentFilter completedFilter = new IntentFilter(
				ACTION_DATA_REFRESH_COMPLETED);
		registerReceiver(br, completedFilter);
		super.onResume();
	}

	@Override
	protected void onPause() {
		try {
			unregisterReceiver(br);

		} catch (IllegalArgumentException e) {
			// nothing to do
		}
		super.onPause();
	}

	@Override
	public void onRefreshButtonClick(RefreshActionItem sender) {
		mRefreshActionItem.showProgress(true);
		dataKeeper.refresh(true);
	}
}
