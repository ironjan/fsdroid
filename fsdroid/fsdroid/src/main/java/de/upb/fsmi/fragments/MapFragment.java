package de.upb.fsmi.fragments;

import android.support.v4.app.*;

import com.googlecode.androidannotations.annotations.*;

import de.upb.fsmi.*;
import de.upb.fsmi.views.*;

@EFragment(R.layout.fragment_map)
public class MapFragment extends Fragment {
	@ViewById
	MyMapView map;
	
	
}
