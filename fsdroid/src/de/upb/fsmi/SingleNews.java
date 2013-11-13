package de.upb.fsmi;

import android.annotation.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.util.*;

import com.googlecode.androidannotations.annotations.*;

import de.upb.fsmi.fragments.*;

@EActivity(R.layout.activity_news_details)
public class SingleNews extends ActionBarActivity {
	@Extra
	long news_id;

	
	public static final String EXTRA_NEWS_ID = "news_id";

	@FragmentById
	NewsDetailsFragment newsDetailsFragment;

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

	@AfterViews
	void passToFragment() {
		Log.v(SingleNews.class.getSimpleName(), "news_id = " + news_id);
		newsDetailsFragment.displayNewsItemFromId(news_id);
	}

}
