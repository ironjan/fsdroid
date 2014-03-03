package de.upb.fsmi.fsdroid.fragments;

import android.content.*;
import android.database.*;
import android.net.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.text.*;
import android.text.method.*;
import android.text.util.*;
import android.util.*;
import android.widget.*;

import org.androidannotations.annotations.*;
import org.androidannotations.annotations.res.*;
import org.slf4j.*;

import java.text.*;
import java.text.ParseException;
import java.util.*;

import de.upb.fsmi.fsdroid.*;
import de.upb.fsmi.fsdroid.sync.*;

@EFragment(R.layout.fragment_news_details)
public class NewsDetailsFragment extends Fragment {

    @SuppressWarnings("static-access")
    private static final DateFormat SDF = SimpleDateFormat
            .getDateTimeInstance();

    private static final SimpleDateFormat DATE_FORMAT = NewsItemContract.NewsItemColumns.DATE_FORMAT;

    @ViewById
    TextView newsTitle, newsDate, newsContent;

    @StringRes
    String news;
    private String mTitle;
    private Date mDate;
    private String mContent;

    Logger LOGGER = LoggerFactory.getLogger(NewsDetailsFragment.class.getSimpleName());

    @Override
    public void onResume() {
        super.onResume();
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setTitle(news);
    }

    @Background
    public void displayNewsItemFromId(long pNews_id) {
        Log.v(NewsDetailsFragment.class.getSimpleName(), "Id: " + pNews_id); //$NON-NLS-1$
        fetchNewsItem(pNews_id);
        bind();
    }

    @UiThread
    void bind() {
        newsTitle.setText(mTitle);
        newsDate.setText(SDF.format(mDate));
        newsContent.setText(Html.fromHtml(mContent));
        newsContent.setMovementMethod(LinkMovementMethod.getInstance());
        Linkify.addLinks(newsContent, Linkify.WEB_URLS);
    }

    private void fetchNewsItem(long pNews_id) {
        String[] projection = new String[]{NewsItemContract.NewsItemColumns.COLUMN_TITLE,
                NewsItemContract.NewsItemColumns.COLUMN_DATE,
                NewsItemContract.NewsItemColumns.COLUMN_CONTENT,
                NewsItemContract.NewsItemColumns.COLUMN_ID};

        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor =
                resolver.query(Uri.withAppendedPath(NewsItemContract.NEWS_URI, "" + pNews_id),
                        projection, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                mTitle = cursor.getString(0);
                try {
                    mDate = DATE_FORMAT.parse(cursor.getString(1));
                } catch (ParseException e) {
                    // Should never happen
                    Log.e("NewsDetailsFragment", e.getMessage(), e);
                }
                mContent = cursor.getString(2);
            } while (cursor.moveToNext());
        }
    }

}
