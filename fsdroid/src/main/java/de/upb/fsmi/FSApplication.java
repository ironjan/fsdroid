package de.upb.fsmi;

import android.app.Application;

import com.googlecode.androidannotations.annotations.EApplication;

import de.upb.fsmi.db.DatabaseManager;

@EApplication
public class FSApplication extends Application {

	@Override
	public void onCreate() {
		DatabaseManager.init(getApplicationContext());
	};
}
