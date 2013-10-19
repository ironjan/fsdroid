package de.upb.fsmi.fragments;

import android.support.v4.app.*;
import android.support.v7.app.*;

import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.annotations.res.*;

import de.upb.fsmi.*;

@EFragment(R.layout.fragment_overview)
public class OverviewFragment extends Fragment {
	@StringRes
	String misc;

	@AfterViews
	void updateTitle() {
		((ActionBarActivity) getActivity()).getSupportActionBar()
				.setTitle(misc);
	}
}
