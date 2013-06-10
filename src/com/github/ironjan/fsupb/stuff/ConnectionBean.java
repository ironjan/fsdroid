package com.github.ironjan.fsupb.stuff;

import android.annotation.*;
import android.net.*;
import android.os.*;

import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.api.*;

@EBean(scope = Scope.Singleton)
public class ConnectionBean {

	@SystemService
	ConnectivityManager cm;

	public boolean hasInternetConnection() {
		return (cm.getActiveNetworkInfo() != null);
	}

	@SuppressLint("InlinedApi")
	public boolean hasWifiOrEthernet() {
		NetworkInfo info = cm.getActiveNetworkInfo();

		if (info == null) {
			return false;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			return info.getType() == ConnectivityManager.TYPE_WIFI
					|| info.getType() == ConnectivityManager.TYPE_ETHERNET;
		}

		return info.getType() == ConnectivityManager.TYPE_WIFI;
	}
}
