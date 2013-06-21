package com.github.ironjan.fsupb.fragments;

import java.text.*;
import java.util.*;

import android.content.*;
import android.util.*;
import android.widget.*;

import com.actionbarsherlock.app.*;
import com.github.ironjan.fsupb.*;
import com.github.ironjan.fsupb.model.*;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.*;

@EFragment(R.layout.fragment_test)
public class TestFragment extends SherlockFragment {

	private static final String TAG = TestFragment.class.getSimpleName();
	@ViewById
	TextView txtDate, txtStatus;

	@ViewById
	ImageView imgStatus;

	@StringArrayRes
	String[] stati;

	@Bean
	DataKeeper dataKeeper;
	BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			refreshDisplayedData();
		}
	};

	@Override
	public void onActivityCreated(android.os.Bundle savedInstanceState) {
		dataKeeper.refresh(false);
		IntentFilter filter = new IntentFilter(
				DataKeeper.ACTION_DATA_REFRESH_COMPLETED);
		getActivity().registerReceiver(br, filter);

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onPause() {
		try {
			getActivity().unregisterReceiver(br);
		} catch (IllegalArgumentException e) {// nothing to do
		}
		super.onPause();
	}

	protected void refreshDisplayedData() {
		updateDate(dataKeeper.getNextMeetingDate());
		updateStatus(dataKeeper.getFsmiState());
	}

	private void updateStatus(int fsmiState) {
		imgStatus.setImageLevel(fsmiState);
		txtStatus.setText(stati[fsmiState]);
	}

	void logError(Exception e) {
		Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
		Log.e(TAG, e.getMessage(), e);
	}

	@UiThread
	public void updateDate(Date date) {
		DateFormat df = DateFormat.getDateTimeInstance();
		txtDate.setText(df.format(date));
	}

}
