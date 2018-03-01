package com.example.jcgut.notethunder;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jcgut on 02/27/2018.
 */

class MemoDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE = "MemosDB";
    public static final String TABLE = "Memos";
    public static final int VERSION = 1;
    Context context;

    public MemoDatabaseOpenHelper(Context context){
        super(context,DATABASE,null,VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+TABLE+"(dates INTEGER PRIMARY KEY, memo TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}
