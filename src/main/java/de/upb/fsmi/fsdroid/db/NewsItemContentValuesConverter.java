package de.upb.fsmi.fsdroid.db;

import android.content.*;

import java.text.*;

import de.upb.fsmi.fsdroid.sync.*;
import de.upb.fsmi.fsdroid.sync.entities.*;

public class NewsItemContentValuesConverter implements ContentValuesConverter<NewsItem> {
    private static final SimpleDateFormat DATE_FORMAT = NewsItemContract.NewsItemColumns.DATE_FORMAT;

    @Override
    public ContentValues convert(NewsItem newsItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsItemContract.NewsItemColumns.COLUMN_LINK, newsItem.getLink());
        contentValues.put(NewsItemContract.NewsItemColumns.COLUMN_DATE, DATE_FORMAT.format(newsItem.getDate()));
        contentValues.put(NewsItemContract.NewsItemColumns.COLUMN_AUTHOR, newsItem.getAuthor());
        contentValues.put(NewsItemContract.NewsItemColumns.COLUMN_CONTENT, newsItem.getContent());
        contentValues.put(NewsItemContract.NewsItemColumns.COLUMN_DESCRIPTION, newsItem.getDescription());
        contentValues.put(NewsItemContract.NewsItemColumns.COLUMN_TITLE, newsItem.getTitle());

        return contentValues;
    }

    @Override
    public ContentValues convertAddId(NewsItem newsItem) {
        final ContentValues contentValues = convert(newsItem);
        contentValues.put(NewsItemContract.NewsItemColumns.COLUMN_ID, newsItem.get_id());
        return contentValues;
    }
}
