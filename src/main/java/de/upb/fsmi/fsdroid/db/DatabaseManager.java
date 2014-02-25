package de.upb.fsmi.fsdroid.db;

import android.content.*;
import android.util.*;

import com.j256.ormlite.dao.*;
import com.j256.ormlite.stmt.*;

import java.sql.*;
import java.util.*;

import de.upb.fsmi.fsdroid.*;
import de.upb.fsmi.fsdroid.sync.*;

public class DatabaseManager {

    private static final String TAG = DatabaseManager.class.getSimpleName();
    static private DatabaseManager instance;

    static public DatabaseManager getInstance(Context context) {
        if (null == instance) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    private DatabaseHelper helper;

    private DatabaseManager(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    public DatabaseHelper getHelper() {
        return helper;
    }

    public List<NewsItem> getAllNewsItems() {
        String tag = "getAllNewsItems(): ";
        List<NewsItem> newsItems = null;
        try {
            newsItems = (List<NewsItem>) getHelper().getNewsItemDao().queryForAll();
            Collections.sort(newsItems);
            Log.d(TAG, tag + "Found " + newsItems.size() + " news items.");
        } catch (SQLException e) {
            Log.e(TAG, tag + e.getMessage(), e);
        }
        return newsItems;
    }

    public boolean createOrUpdate(NewsItem pNewsItem) {
        String tag = "createOrUpdate(..): ";
        try {
            Dao<NewsItem, Integer> newsItemDao = getHelper().getNewsItemDao();
            QueryBuilder<NewsItem, Integer> queryBuilder = newsItemDao
                    .queryBuilder();
            PreparedQuery<NewsItem> preparedQuery = queryBuilder.where()
                    .eq(NewsItemContract.NewsItemColumns.COLUMN_LINK, pNewsItem.getLink()).prepare();
            List<NewsItem> query = newsItemDao.query(preparedQuery);
            if (BuildConfig.DEBUG) Log.v(TAG, tag + "Found " + query.size() + " news items");
            if (query.size() == 1) {
                pNewsItem.set_id(query.get(0).get_id());
                newsItemDao.update(pNewsItem);
                if (BuildConfig.DEBUG) Log.v(TAG, tag + "Updated news item.");
            } else {
                newsItemDao.create(pNewsItem);
                if (BuildConfig.DEBUG) Log.v(TAG, tag + "Created news item.");
            }
            return true;
        } catch (SQLException e) {
            Log.e(TAG, tag + e.getMessage(), e);
        }
        Log.d(TAG, tag + "Nothing created or updated.");
        return false;
    }

    @SuppressWarnings("boxing")
    public NewsItem getNewsItemByID(long pNews_id) {
        String tag = "getNewsItemByID(" + pNews_id + "): ";
        try {
            Dao<NewsItem, Integer> newsItemDao = getHelper().getNewsItemDao();
            QueryBuilder<NewsItem, Integer> queryBuilder = newsItemDao
                    .queryBuilder();
            PreparedQuery<NewsItem> preparedQuery = queryBuilder.where()
                    .eq(NewsItemContract.NewsItemColumns.COLUMN_ID, pNews_id).prepare();
            List<NewsItem> query = newsItemDao.query(preparedQuery);

            Log.d(TAG, tag + "Created one item");
            if (query.size() == 1) {
                return query.get(0);
            }
            Log.d(TAG, tag + "Error on creation.");
        } catch (SQLException e) {
            Log.e(TAG, tag + e.getMessage(), e);
        }
        Log.d(TAG, tag + "Found nothing.");
        return null;
    }

    public void clearNews() {
        try {
            final int delete = getHelper().getNewsItemDao().deleteBuilder().delete();
            Log.i(TAG, "Deleted " + delete + " news.");
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }
}
