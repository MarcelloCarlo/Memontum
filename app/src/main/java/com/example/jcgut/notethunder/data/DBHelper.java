package com.example.jcgut.notethunder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.example.jcgut.notethunder.domain.Memo;

import java.sql.SQLException;



public class DBHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "talahanayan.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    /*
    Ito ay tinatawag na kung ang talahanayan.db file ay hindi nilikha sa sobrang (konteksto ...) na tinatawag sa tagapagbuo
     */

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            // Lumikha ng Table
            TableUtils.createTable(connectionSource, Memo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tinatawag na pagbabago ng bersyon int
    // Tawagan ang onCreate internally pagkatapos ng tawag
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            // Tanggalin ang talahanayan
            TableUtils.dropTable(connectionSource, DBHelper.class, false);
            /*
            * Lohika na may kaugnayan sa pagpapanatili ng data
            * */
            // Gumawa ng bagong talahanayan
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
