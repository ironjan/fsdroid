package de.upb.fsmi.fsdroid.db;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.*;

import com.j256.ormlite.android.apptools.*;
import com.j256.ormlite.dao.*;
import com.j256.ormlite.support.*;
import com.j256.ormlite.table.*;

import de.upb.fsmi.fsdroid.sync.*;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    // name of the database file for your application -- change to something
    // appropriate for your app
    private static final String DATABASE_NAME = "fsdroid.sqlite";

    // any time you make changes to your database objects, you may have to
    // increase the database version
    private static final int DATABASE_VERSION = 4;

    // the DAO object we use to access the SimpleData table
    private Dao<NewsItem, Integer> wishListDao = null;

    protected DatabaseHelper(Context context) {
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
            switch (pArg3) {
                case 1:
                case 2:
                    TableUtils.dropTable(pArg1, OldNewsItem.class, true);
                case 3:
                    TableUtils.createTable(pArg1, SyncInfo.class);
            }
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


    @DatabaseTable(tableName = "newsItems")
    private static class OldNewsItem {
    }
}
