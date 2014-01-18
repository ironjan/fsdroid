package de.upb.fsmi.fragments;

import android.support.v4.app.*;
import android.support.v7.app.*;
import android.text.*;
import android.util.*;
import android.widget.*;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.res.*;

import java.text.*;

import de.upb.fsmi.*;
import de.upb.fsmi.db.*;
import de.upb.fsmi.sync.*;

@EFragment(R.layout.fragment_news_details)
public class NewsDetailsFragment extends Fragment {

    @SuppressWarnings("static-access")
    private static final DateFormat SDF = SimpleDateFormat
            .getDateTimeInstance();

    @ViewById
    TextView newsTitle, newsDate, newsContent;

    @StringRes
    String news;

    @Override
    public void onResume() {
        super.onResume();
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setTitle(news);
    }

    @Background
    public void displayNewsItemFromId(long pNews_id) {
        Log.v(NewsDetailsFragment.class.getSimpleName(), "Id: " + pNews_id); //$NON-NLS-1$
        NewsItem newsItem = fetchNewsItem(pNews_id);
        bind(newsItem);
    }

    @UiThread
    void bind(NewsItem pNewsItem) {
        newsTitle.setText(pNewsItem.getTitle());
        newsDate.setText(SDF.format(pNewsItem.getDate()));
        newsContent.setText(Html.fromHtml(pNewsItem.getContent()));
    }

    private NewsItem fetchNewsItem(long pNews_id) {
        DatabaseManager instance = DatabaseManager.getInstance(getActivity());
        NewsItem newsItem = instance.getNewsItemByID(pNews_id);
        return newsItem;
    }

}
