package de.upb.fsmi.fsdroid.sync;

import android.net.*;
import android.provider.*;

import java.text.*;
import java.util.*;

/**
 * Created by ljan on 18.01.14.
 */
public class NewsItemContract {
    public static final String NEWS_ITEMS_TABLE = "news";
    private static final String PATH_DIVIDER = "/";

    public static final String AUTHORITY = AccountCreator.AUTHORITY;

    public static final String NEWS_PATH = "news";
    public static final String SINGLE_NEWS_PATH = "news" + PATH_DIVIDER + "#";

    public static final String NEWS_ABSOLUTE_URI_PATH = "content://" + AUTHORITY + PATH_DIVIDER + NEWS_PATH;

    public static final Uri NEWS_URI = Uri.parse(NEWS_ABSOLUTE_URI_PATH);

    public static final Uri SINGLE_NEWS_URI = Uri.withAppendedPath(NEWS_URI, "#");


    public static class NewsItemColumns {
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_DATE = "date";
        public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TITLE = "title";

        public static final String[] AVAILABLE_COLUMNS = {COLUMN_LINK, COLUMN_ID, COLUMN_DATE,
                COLUMN_AUTHOR, COLUMN_CONTENT, COLUMN_DESCRIPTION, COLUMN_TITLE};
    }

    public static String[] NEWS_LIST_PROJECTION = {NewsItemColumns.COLUMN_TITLE, NewsItemColumns.COLUMN_DESCRIPTION, NewsItemColumns.COLUMN_ID};

    public static void checkNewsItemColumnsProjection(String[] projection) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(NewsItemColumns.AVAILABLE_COLUMNS));

            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
