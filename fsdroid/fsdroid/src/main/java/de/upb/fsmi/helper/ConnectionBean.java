package de.upb.fsmi.helper;

import android.annotation.*;
import android.net.*;
import android.os.*;

import com.googlecode.androidannotations.annotations.*;
import com.googlecode.androidannotations.api.*;

@EBean(scope = Scope.Singleton)
public class ConnectionBean {

	@SystemService
	ConnectivityManager cm;

	public boolean isNetworkAvailable() {
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
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
