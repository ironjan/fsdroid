package com.github.ironjan.fsupb.fragments;

import java.text.*;
import java.util.*;

import android.util.*;
import android.widget.*;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;
import com.github.ironjan.fsupb.*;
import com.github.ironjan.fsupb.stuff.*;
import com.googlecode.androidannotations.annotations.*;

@EFragment(R.layout.fragment_test)
@OptionsMenu(R.menu.menu_main)
public class TestFragment extends SherlockFragment {

	private static final String TAG = TestFragment.class.getSimpleName();
	@ViewById
	TextView txtDownload, txtDate, txtStatus;
	private MenuItem abRefresh;

	@ViewById
	ImageView imgStatus;

	@Bean
	StatusBean statusBean;
	@Bean
	MeetingBean meetingBean;

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
		refresh();
	}

	@Background
	void refresh() {
		showProgressInActionBar();
		refreshStatus();
		refreshMeeting();
		hideProgressInActionBar();
	}

	@Background
	void refreshMeeting() {
		meetingBean.refreshDate(this);
	}

	void logError(Exception e) {
		Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
		Log.e(TAG, e.getMessage(), e);
	}

	@UiThread
	void showProgressInActionBar() {
		Log.v(TAG, "showProgressInActionBar");
		SherlockFragmentActivity activity = getSherlockActivity();
		activity.setProgressBarIndeterminate(true);
		activity.setProgressBarIndeterminateVisibility(true);

		Log.v(TAG, "abRefresh: " + abRefresh);
		if (abRefresh != null) {
			abRefresh.setVisible(false);
		}
	}

	@UiThread
	void hideProgressInActionBar() {
		Log.v(TAG, "hideProgressInActionBar");
		SherlockFragmentActivity activity = getSherlockActivity();
		activity.setProgressBarIndeterminate(false);
		activity.setProgressBarIndeterminateVisibility(false);

		Log.v(TAG, "abRefresh: " + abRefresh);
		if (abRefresh != null) {
			abRefresh.setVisible(true);
		}
	}

	@UiThread
	void refreshStatus() {
		statusBean.refreshStatus(this);
	}

	@UiThread
	public void setStatus(int status) {
		txtStatus.setText("" + status);
		imgStatus.setImageLevel(status);
	}

	@UiThread
	public void updateDate(Date date) {
		DateFormat df = DateFormat.getDateTimeInstance();
		txtDate.setText(df.format(date));
	}

	@UiThread
	void updateProgress(int downloadedSize, int totalSize) {
		txtDownload.setText(downloadedSize + "/" + totalSize);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (menu != null) {
			this.abRefresh = menu.findItem(R.id.ab_refresh);
		}

		super.onCreateOptionsMenu(menu, inflater);
	}

	@OptionsItem(R.id.ab_refresh)
	void actionRefresh() {
		txtDate.setText("");
		txtStatus.setText("");
		imgStatus.setImageLevel(0);
		refresh();
	}
}
