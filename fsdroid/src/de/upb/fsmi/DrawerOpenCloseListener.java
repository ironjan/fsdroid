package de.upb.fsmi;

import android.app.*;
import android.support.v4.app.*;
import android.support.v4.widget.*;
import android.util.*;
import android.view.*;

final class DrawerOpenCloseListener extends ActionBarDrawerToggle {
	DrawerOpenCloseListener(Activity activity, DrawerLayout drawerLayout, int drawerImageRes,
			int openDrawerContentDescRes, int closeDrawerContentDescRes) {
		super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes,
				closeDrawerContentDescRes);
	}

	@Override
	public void onDrawerClosed(View view) {
		Log.v(FSDroid.TAG, "drawer closed");
		super.onDrawerClosed(view);
	}

	@Override
	public void onDrawerOpened(View drawerView) {
		Log.v(FSDroid.TAG, "Drawer opened");
		super.onDrawerOpened(drawerView);
	}
}