package de.upb.fsmi;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;

import de.upb.fsmi.fragments.NewsDetailsFragment;

@EActivity(R.layout.activity_news_details)
public class SingleNews extends ActionBarActivity {
	@Extra
	long news_id;
	public static final String EXTRA_NEWS_ID = "news_id";

	@FragmentById
	NewsDetailsFragment newsDetailsFragment;

	@AfterViews
	void passToFragment() {
		Log.v(SingleNews.class.getSimpleName(), "news_id = " + news_id);
		newsDetailsFragment.displayNewsItemFromId(news_id);
	}
}
