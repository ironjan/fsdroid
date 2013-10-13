package de.upb.fsmi.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.fima.cardsui.objects.Card;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.res.StringRes;

import de.upb.fsmi.cards.views.NewsCardView;
import de.upb.fsmi.cards.views.NewsCardView_;
import de.upb.fsmi.news.persistence.NewsItem;

@EBean
public class NewsCard extends Card {

	private NewsItem mRssItem;

	@StringRes
	String newsLoadingTitle;

	@Override
	public View getCardContent(Context pContext) {
		final String title = (mRssItem != null) ? mRssItem.getTitle()
				: newsLoadingTitle;

		NewsCardView view = NewsCardView_.build(title, pContext);
		view.bind(mRssItem);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		return view;
	}

	public void bind(NewsItem pItem) {
		mRssItem = pItem;
	}

}
