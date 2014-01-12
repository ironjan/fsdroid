package de.upb.fsmi.cards.views;

import android.content.*;
import android.util.*;
import android.widget.*;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.res.*;

import de.upb.fsmi.*;
import de.upb.fsmi.news.persistence.*;

@EViewGroup(R.layout.card_news_item)
public class NewsCardView extends RelativeLayout {
	@ViewById
	TextView newsHeadline, newsText;

	@StringRes
	String newsLoadingTitle, newsLoadingDescription;

	private NewsItem mRssItem;

	public NewsCardView(String title, Context pContext) {
		super(pContext);
	}

	public void bind(NewsItem pRssItem) {
		mRssItem = pRssItem;
		if (pRssItem != null) {
			newsHeadline.setText(pRssItem.getTitle());
			displayItemContent(pRssItem.getDescription());
		} else {
			newsHeadline.setText(newsLoadingTitle);
			newsText.setText(newsLoadingDescription);
		}
	}

	@SuppressWarnings("nls")
	private void displayItemContent(String pString) {
		// bug in WebView.loadData(data, mimeType, encoding) which fucks up
		// utf-8
		final String description = pString;
		newsText.setText(description);
	}

	@Click({ R.id.newsHeadline, R.id.newsText })
	void showNewsDetails() {
		long _id = mRssItem.get_id();
		Log.v(NewsCardView.class.getSimpleName(), "Clicked on " + _id);
		SingleNews_.intent(getContext()).news_id(_id).start();
	}

}
