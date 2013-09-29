package de.upb.fsmi.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.fima.cardsui.objects.Card;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;

import de.upb.fsmi.cards.views.NewsCardView;
import de.upb.fsmi.cards.views.NewsCardView_;

public class NewsCard extends Card {

	private Item mRssItem;

	@Override
	public View getCardContent(Context pContext) {
		final String title = (mRssItem != null) ? mRssItem.getTitle() : "null item";
		NewsCardView view = NewsCardView_.build(title,pContext);
		view.bind(mRssItem);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		return view;
	}

	public void bind(Item pRssItem) {
		mRssItem = pRssItem;
	}

}
