package de.upb.fsmi.cards.views;

import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;

import de.upb.fsmi.R;
import de.upb.fsmi.SingleNews_;
import de.upb.fsmi.news.persistence.NewsItem;

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
