package de.upb.fsmi.views;

import org.osmdroid.tileprovider.tilesource.*;
import org.osmdroid.views.*;

import android.content.*;
import android.util.*;

import org.androidannotations.annotations.*;

@EView
public class MyMapView extends MapView {

	public MyMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setMultiTouchControls(true);
		setTileSource(TileSourceFactory.MAPNIK);
		setBuiltInZoomControls(true);
	}

}
