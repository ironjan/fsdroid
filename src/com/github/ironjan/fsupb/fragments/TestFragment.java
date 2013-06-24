package com.github.ironjan.fsupb.fragments;

import java.text.*;
import java.util.*;

import android.util.*;
import android.widget.*;

import com.actionbarsherlock.app.*;
import com.github.ironjan.fsupb.*;
import com.github.ironjan.fsupb.model.*;
import com.github.ironjan.fsupb.receiver.*;
import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.*;

@EFragment(R.layout.fragment_test)
public class TestFragment extends SherlockFragment implements
		UpdateCompletedListener {

	private static final String TAG = TestFragment.class.getSimpleName();
	@ViewById
	TextView txtDate, txtStatus;

	@ViewById
	ImageView imgStatus;

	@StringArrayRes
	String[] stati;

	@Bean
	DataKeeper dataKeeper;

	UpdateCompletedReceiver updateCompletedReceiver = new UpdateCompletedReceiver(
			this);

	@Override
	public void onResume() {
		updateCompletedReceiver.registerReceiver(getActivity()
				.getApplicationContext());
		super.onResume();
	}

	@Override
	public void onPause() {
		updateCompletedReceiver.unregisterReceiver();
		super.onPause();
	}

	@AfterViews
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
		if (date != null) {
			DateFormat df = DateFormat.getDateTimeInstance();
			txtDate.setText(df.format(date));
		} else {
			txtDate.setText("unbekannt");
		}
	}

	@Override
	public void updateCompleted() {
		refreshDisplayedData();
	}

}
