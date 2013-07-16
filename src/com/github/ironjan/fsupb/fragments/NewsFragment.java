package com.github.ironjan.fsupb.fragments;

import android.util.*;
import android.widget.*;

import com.actionbarsherlock.app.*;
import com.fima.cardsui.views.*;
import com.github.ironjan.fsupb.*;
import com.github.ironjan.fsupb.cards.*;
import com.github.ironjan.fsupb.model.*;
import com.github.ironjan.fsupb.receiver.*;
import com.googlecode.androidannotations.annotations.*;

@EFragment(R.layout.fragment_news)
public class NewsFragment extends SherlockFragment implements
		UpdateCompletedListener {

	private static final String TAG = NewsFragment.class.getSimpleName();

	@ViewById
	CardUI cardsview;

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
	@UiThread
	protected void refreshDisplayedData() {
		cardsview.setSwipeable(true);
		cardsview.addCard(new StatusCard(dataKeeper.getFsmiState()));
		cardsview.addCard(new MeetingCard(dataKeeper.getNextMeetingDate()));
		cardsview.refresh();
	}

	void logError(Exception e) {
		Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
		Log.e(TAG, e.getMessage(), e);
	}

	@Override
	public void updateCompleted() {
		refreshDisplayedData();
	}

}
