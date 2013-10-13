package de.upb.fsmi.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import de.upb.fsmi.news.persistence.NewsItem;

class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	// name of the database file for your application -- change to something
	// appropriate for your app
	private static final String DATABASE_NAME = "WishListDB.sqlite";

	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 2;

	// the DAO object we use to access the SimpleData table
	private Dao<NewsItem, Integer> wishListDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase pArg0, ConnectionSource pArg1) {
		try {
			TableUtils.createTable(connectionSource, NewsItem.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase pArg0, ConnectionSource pArg1,
			int pArg2, int pArg3) {
		try {
			TableUtils.dropTable(pArg1, NewsItem.class, true);
			onCreate(pArg0);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
	}

	public Dao<NewsItem, Integer> getNewsItemDao() {
		if (null == wishListDao) {
			try {
				wishListDao = getDao(NewsItem.class);
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		return wishListDao;
	}

}
