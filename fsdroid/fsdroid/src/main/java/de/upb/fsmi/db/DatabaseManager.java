package de.upb.fsmi.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import de.upb.fsmi.news.persistence.NewsItem;

public class DatabaseManager {

	private static final String TAG = DatabaseManager.class.getSimpleName();
	static private DatabaseManager instance;

	static public void init(Context ctx) {
		if (null == instance) {
			instance = new DatabaseManager(ctx);
		}
	}

	static public DatabaseManager getInstance() {
		return instance;
	}

	private DatabaseHelper helper;

	private DatabaseManager(Context ctx) {
		helper = new DatabaseHelper(ctx);
	}

	private DatabaseHelper getHelper() {
		return helper;
	}

	public List<NewsItem> getAllNewsItems() {
		List<NewsItem> newsItems = null;
		try {
			newsItems = getHelper().getNewsItemDao().queryForAll();
		} catch (SQLException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return newsItems;
	}

	public boolean createOrUpdate(NewsItem pNewsItem) {
		try {
			Dao<NewsItem, Integer> newsItemDao = getHelper().getNewsItemDao();
			QueryBuilder<NewsItem, Integer> queryBuilder = newsItemDao
					.queryBuilder();
			PreparedQuery<NewsItem> preparedQuery = queryBuilder.where()
					.eq(NewsItem.COLUMN_LINK, pNewsItem.getLink()).prepare();
			List<NewsItem> query = newsItemDao.query(preparedQuery);
			if (query.size() == 1) {
				newsItemDao.delete(query.get(0));
			}

			int created = newsItemDao.create(pNewsItem);

			return (1 == created);
		} catch (SQLException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return false;
	}

	@SuppressWarnings("boxing")
	public NewsItem getNewsItemByID(long pNews_id) {
		try {
			Dao<NewsItem, Integer> newsItemDao = getHelper().getNewsItemDao();
			QueryBuilder<NewsItem, Integer> queryBuilder = newsItemDao
					.queryBuilder();
			PreparedQuery<NewsItem> preparedQuery = queryBuilder.where()
					.eq(NewsItem.COLUMN_ID, pNews_id).prepare();
			List<NewsItem> query = newsItemDao.query(preparedQuery);

			if (query.size() == 1) {
				return query.get(0);
			}
		} catch (SQLException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return null;
	}
}
