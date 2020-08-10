package com.example.jcgut.notethunder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.jcgut.notethunder.domain.Memo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;



public class DBHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "talahanayan.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Memo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {

            TableUtils.dropTable(connectionSource, DBHelper.class, false);

            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Dao<Memo, Long> memoDao = null;

    public Dao<Memo, Long> getMemoDao() throws SQLException {
        if(memoDao ==null) {
            memoDao = getDao(Memo.class);
        }
        return memoDao;
    }

    public void dropDao() {
        if(memoDao !=null) {
            memoDao = null;
        }
    }
}
