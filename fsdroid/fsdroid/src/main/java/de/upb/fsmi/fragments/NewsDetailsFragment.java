package de.upb.fsmi.fragments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

import de.upb.fsmi.R;
import de.upb.fsmi.db.DatabaseManager;
import de.upb.fsmi.news.persistence.NewsItem;

@EFragment(R.layout.fragment_news_details)
public class NewsDetailsFragment extends Fragment {

	private static final DateFormat SDF = SimpleDateFormat
			.getDateTimeInstance();

	@ViewById
	TextView newsTitle, newsDate, newsContent;

	@Background
	public void displayNewsItemFromId(long pNews_id) {
		Log.v(NewsDetailsFragment.class.getSimpleName(), "Id: " + pNews_id);

		NewsItem newsItem = fetchNewsItem(pNews_id);
		bind(newsItem);
	}

	@UiThread
	void bind(NewsItem pNewsItem) {
		newsTitle.setText(pNewsItem.getTitle());
		newsDate.setText(SDF.format(pNewsItem.getDate()));
		newsContent.setText(Html.fromHtml(pNewsItem.getContent()));
	}

	private static NewsItem fetchNewsItem(long pNews_id) {
		DatabaseManager instance = DatabaseManager.getInstance();
		NewsItem newsItem = instance.getNewsItemByID(pNews_id);
		return newsItem;
	}

}
