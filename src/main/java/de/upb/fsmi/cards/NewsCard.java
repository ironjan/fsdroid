package de.upb.fsmi.cards;

import android.content.*;
import android.view.*;
import android.view.ViewGroup.*;

import com.fima.cardsui.objects.*;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.res.*;

import de.upb.fsmi.cards.views.*;
import de.upb.fsmi.sync.*;

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
