package com.github.ironjan.fsupb;

import android.os.*;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;
import com.googlecode.androidannotations.annotations.*;

@EActivity(R.layout.activity_test)
public class Test extends SherlockFragmentActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		hideProgressInActionBar();
		super.onCreate(arg0);
	}

	public void hideProgressInActionBar() {
		setProgressBarIndeterminate(false);
		setProgressBarIndeterminateVisibility(false);
	}

}
