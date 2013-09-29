package de.upb.fsmi.cards.views;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Description;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

import de.upb.fsmi.R;

@EViewGroup(R.layout.card_news_item)
public class NewsCardView extends RelativeLayout {
	@ViewById
	TextView newsHeadline, newsText;

	public NewsCardView(String title, Context pContext) {
		super(pContext);
	}

	public void bind(Item pRssItem) {
		newsHeadline.setText(pRssItem.getTitle());
		displayItemContent(pRssItem.getDescription());
	}

	@SuppressWarnings("nls")
	private void displayItemContent(Description pDescription) {
		// bug in WebView.loadData(data, mimeType, encoding) which fucks up
		// utf-8
		final String description = pDescription.getValue();
		newsText.setText(description);
	}

}
