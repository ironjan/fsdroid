package de.upb.fsmi.fragments;

import android.support.v4.app.*;
import android.support.v7.app.*;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.res.*;

import de.upb.fsmi.*;
import de.upb.fsmi.views.*;

@EFragment(R.layout.fragment_map)
public class MapFragment extends Fragment {
	@ViewById(R.id.map)
	MyMapView mapView;

	
	@StringRes
	String map;

	@Override
	public void onResume() {
		super.onResume();
		((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(map);
	}
}
