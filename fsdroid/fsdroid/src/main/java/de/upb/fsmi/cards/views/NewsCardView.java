package de.upb.fsmi.cards.views;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Description;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;

import de.upb.fsmi.R;

@EViewGroup(R.layout.card_news_item)
public class NewsCardView extends RelativeLayout {
	@StringRes
	String die_fachschaft_de;

	@ViewById
	TextView newsHeadline, newsDate;
	@ViewById
	WebView newsText;
	@ViewById
	ImageView imgNewsLink;

	private String mLink;

	@Click({ R.id.imgNewsLink })
	void goToWebsite() {
		if ("".equals(mLink)) {
			imgNewsLink.setVisibility(View.GONE);
			return;
		}
		Uri fsmiWebsite = Uri.parse(mLink);
		Intent intent = new Intent(Intent.ACTION_VIEW, fsmiWebsite);
		getContext().startActivity(intent);
	}

	public NewsCardView(String title, Context pContext) {
		super(pContext);
	}

	public void bind(Item pRssItem) {
		this.mLink = pRssItem.getLink();
		newsHeadline.setText(pRssItem.getTitle());
		newsDate.setText(SimpleDateFormat.getDateTimeInstance().format(
				pRssItem.getPubDate()));
		displayItemContent(pRssItem.getDescription());
	}

	private void displayItemContent(Description pDescription) {
		// bug in WebView.loadData(data, mimeType, encoding) which fucks up
		// utf-8
		newsText.loadDataWithBaseURL(null, pDescription.getValue(),
				"text/html", "utf-8", null);
	}

}
