package de.upb.fsmi;

import android.annotation.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;

import com.googlecode.androidannotations.annotations.*;

@EActivity(R.layout.activity_map)
public class Map extends ActionBarActivity {
	static final String TAG = Map.class.getSimpleName();

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@SuppressLint("InlinedApi")
	@OptionsItem(android.R.id.home)
	void up() {
		Intent upIntent = NavUtils.getParentActivityIntent(this);
		if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
			TaskStackBuilder.create(this)
					.addNextIntentWithParentStack(upIntent).startActivities();
		} else {
			NavUtils.navigateUpTo(this, upIntent);
		}
	}
}
