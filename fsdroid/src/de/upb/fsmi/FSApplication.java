package de.upb.fsmi;

import android.app.*;

import com.googlecode.androidannotations.annotations.*;

import de.upb.fsmi.db.*;

@EApplication
public class FSApplication extends Application {

	@Override
	public void onCreate() {
		DatabaseManager.init(getApplicationContext());
	};
}
