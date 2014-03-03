package de.upb.fsmi.fsdroid.sync;

import android.annotation.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;
import android.text.*;

import org.androidannotations.annotations.*;

import de.upb.fsmi.fsdroid.db.*;
import de.upb.fsmi.fsdroid.sync.entities.*;


/**
 * ContentProvider for fsmi data. Following uris are supported:
 *
 * content://de.fsmi.fsdroid.provider/news
 * content://de.fsmi.fsdroid.provider/news/{newsId}
 * content://de.fsmi.fsdroid.provider/status
 * content://de.fsmi.fsdroid.provider/status/{statusId} (probably always 1)
 * content://de.fsmi.fsdroid.provider/meeting
 * content://de.fsmi.fsdroid.provider/meeting/{meetingId} (probably always 1)
 */
@SuppressLint("Registered")
@EProvider
public class FSDroidContentProvider extends ContentProvider {

    private DatabaseHelper mDatabaseHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(0);


    public static final int ALL_NEWS = 1;
    public static final int SINGLE_NEWS = 2;
    public static final int STATUS = 3;
    public static final int MEETING_DATE = 4;

    public static final String AUTHORITY = AccountCreator.AUTHORITY;


    public static final String MEETING_DATE_PATH = "meeting_date";

    public static final String STATUS_PATH = "status";

    private static final String PATH_DIVIDER = "/";

    public static final String STATUS_ABSOLUTE_URI_PATH = "content://" + AUTHORITY + PATH_DIVIDER + STATUS_PATH;
    public static final String MEETING_DATE_ABSOLUTE_URI_PATH = "content://" + AUTHORITY + PATH_DIVIDER + MEETING_DATE_PATH;

    public static final Uri STATUS_URI = Uri.parse(STATUS_ABSOLUTE_URI_PATH);
    public static final Uri MEETING_DATE_URI = Uri.parse(MEETING_DATE_ABSOLUTE_URI_PATH);

    static {
        sUriMatcher.addURI(AUTHORITY, NewsItemContract.NEWS_PATH, ALL_NEWS);
        sUriMatcher.addURI(AUTHORITY, NewsItemContract.NEWS_PATH + "/#", SINGLE_NEWS);
        sUriMatcher.addURI(AUTHORITY, STATUS_PATH, STATUS);
        sUriMatcher.addURI(AUTHORITY, MEETING_DATE_PATH, MEETING_DATE);
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
            case STATUS:
                return queryStatus(uri, projection, selection, selectionArgs, sortOrder);
            case MEETING_DATE:
                return queryMeetingDate(uri, projection, selection, selectionArgs, sortOrder);
        }

        return null;
    }

    private Cursor queryStatus(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        NewsItemContract.checkNewsItemColumnsProjection(projection);

        queryBuilder.setTables(Status.TABLE);

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        queryBuilder.appendWhere(NewsItemContract.NewsItemColumns.COLUMN_ID + "="
                + uri.getLastPathSegment());

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Cursor queryMeetingDate(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        NewsItemContract.checkNewsItemColumnsProjection(projection);

        queryBuilder.setTables(MeetingDate.TABLE);

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        queryBuilder.appendWhere(NewsItemContract.NewsItemColumns.COLUMN_ID + "="
                + uri.getLastPathSegment());

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
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
            case STATUS:
                return insertStatus(contentValues);
            case MEETING_DATE:
                return insertMeetingDate(contentValues);
        }
        return null;
    }

    private Uri insertStatus(ContentValues contentValues) {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        database.delete(Status.TABLE, null, null);
        long _id = database.insert(Status.TABLE, null, contentValues);

        return Uri.withAppendedPath(STATUS_URI, "" + _id);
    }

    private Uri insertMeetingDate(ContentValues contentValues) {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        database.delete(MeetingDate.TABLE, null, null);
        long _id = database.insert(MeetingDate.TABLE, null, contentValues);

        return Uri.withAppendedPath(MEETING_DATE_URI, "" + _id);
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
