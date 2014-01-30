package de.upb.fsmi.sync;

import android.content.UriMatcher;
import android.provider.BaseColumns;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by ljan on 18.01.14.
 */
public class NewsItemContract {
    public static final String AUTHORITY = AccountCreator.AUTHORITY;
    public static final String NEWS_ITEMS_TABLE = "news";
    private static final String PATH_DIVIDER = "/";

    protected static final UriMatcher sUriMatcher = new UriMatcher(0);

    public static final int ALL_NEWS = 1;

    public static final int SINGLE_NEWS = 2;

    public static final String NEWS_PATH = "news";
    public static final String NEWS_URI = "content://" + AUTHORITY + PATH_DIVIDER + NEWS_PATH;

    static {
        sUriMatcher.addURI(AUTHORITY, "news", ALL_NEWS);
        sUriMatcher.addURI(AUTHORITY, "news" + PATH_DIVIDER + "#", SINGLE_NEWS);
    }

    protected interface NewsItemColumns {
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_DATE = "date";
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
