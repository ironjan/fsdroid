package com.github.ironjan.fsupb.fragments;

import java.text.*;
import java.util.*;

import android.util.*;
import android.view.*;
import android.widget.*;

import com.actionbarsherlock.app.*;
import com.github.ironjan.fsupb.*;
import com.github.ironjan.fsupb.stuff.*;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.*;

@EFragment(R.layout.fragment_test)
public class TestFragment extends SherlockFragment implements StatusCallback {

	private static final String TAG = TestFragment.class.getSimpleName();
	@ViewById
	TextView txtDate, txtStatus;

	private View abRefresh = null, abProgress = null;

	@ViewById
	ImageView imgStatus;

	@StringArrayRes
	String[] stati;

	@Bean
	StatusBean statusBean;
	@Bean
	MeetingBean meetingBean;

	@AfterInject
	void showCustomActionbar() {
		final ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setCustomView(R.layout.action_refresh);
		actionBar.setDisplayShowCustomEnabled(true);

		View customView = actionBar.getCustomView();
		while (abRefresh == null) {
			abRefresh = customView.findViewById(R.id.ab_refresh);
		}
		while (abProgress == null) {
			abProgress = customView.findViewById(android.R.id.progress);
		}

		abRefresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				actionRefresh();
			}
		});
	}

	@AfterViews
	@Background
	void afterViews() {

		long start = System.currentTimeMillis();
		int loopCounter = 0;
		while (abRefresh == null) {
			loopCounter++;
		}

		long time = System.currentTimeMillis() - start;
		Log.v(TAG, "Counted to " + loopCounter + " while waiting " + time
				+ " ms for optionsMenu");
		refresh(false);
	}

	@Background
	void refresh(boolean forced) {
		showProgressInActionBar();
		refreshStatus();
		meetingBean.refreshDate(this, forced);
	}

	void logError(Exception e) {
		Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
		Log.e(TAG, e.getMessage(), e);
	}

	@UiThread
	void showProgressInActionBar() {
		abRefresh.setVisibility(View.INVISIBLE);
		abProgress.setVisibility(View.VISIBLE);
	}

	@UiThread
	void hideProgressInActionBar() {
		abRefresh.setVisibility(View.VISIBLE);
		abProgress.setVisibility(View.INVISIBLE);
	}

	@UiThread
	void refreshStatus() {
		statusBean.refreshStatus(this);
	}

	@Override
	@UiThread
	public void setStatus(int status) {
		imgStatus.setImageLevel(status);
		txtStatus.setText(stati[status]);
	}

	@UiThread
	public void updateDate(Date date) {
		DateFormat df = DateFormat.getDateTimeInstance();
		txtDate.setText(df.format(date));
		hideProgressInActionBar();
	}

	@UiThread
	void updateProgress(int downloadedSize, int totalSize) {
		// do nothing
	}

	@OptionsItem(R.id.ab_refresh)
	void actionRefresh() {
		imgStatus.setImageLevel(0);
		refresh(true);
	}
}
