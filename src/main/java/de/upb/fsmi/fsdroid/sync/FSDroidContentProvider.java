package de.upb.fsmi.fsdroid.sync;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;
import android.text.*;

import org.androidannotations.annotations.*;

import de.upb.fsmi.fsdroid.db.*;


/**
 * A stub content provider needed by the sync framework. No logging.
 */
@EProvider
public class FSDroidContentProvider extends ContentProvider {

    private DatabaseHelper mDatabaseHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(0);


    public static final int ALL_NEWS = 1;
    public static final int SINGLE_NEWS = 2;
    public static final String AUTHORITY = AccountCreator.AUTHORITY;


    static {
        sUriMatcher.addURI(AUTHORITY, NewsItemContract.NEWS_PATH, ALL_NEWS);
        sUriMatcher.addURI(AUTHORITY, NewsItemContract.SINGLE_NEWS_PATH, SINGLE_NEWS);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = DatabaseManager.getInstance(getContext()).getHelper();
        return false;
    }

    @Override
    public synchronized Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) throws NullPointerException, IllegalArgumentException {
        switch (sUriMatcher.match(uri)) {
            case ALL_NEWS:
            case SINGLE_NEWS:
                return queryNews(uri, projection, selection, selectionArgs, sortOrder);
        }

        return null;
    }

    private Cursor queryNews(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        NewsItemContract.checkNewsItemColumnsProjection(projection);

        queryBuilder.setTables(NewsItemContract.NEWS_ITEMS_TABLE);

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {
            case ALL_NEWS:
                if (TextUtils.isEmpty(sortOrder))
                    sortOrder = NewsItemContract.NewsItemColumns.COLUMN_DATE + " DESC";
                break;
            case SINGLE_NEWS:
                queryBuilder.appendWhere(NewsItemContract.NewsItemColumns.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (sUriMatcher.match(uri)) {
            case ALL_NEWS:
                return insertNewsItem(contentValues);
        }
        return null;
    }

    private Uri insertNewsItem(ContentValues contentValues) {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        long _id = database.insert(NewsItemContract.NEWS_ITEMS_TABLE, null, contentValues);
        return Uri.parse(NewsItemContract.NEWS_ABSOLUTE_URI_PATH + "/" + _id);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }


}
