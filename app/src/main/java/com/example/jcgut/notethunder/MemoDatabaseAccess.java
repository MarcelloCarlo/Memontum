package com.example.jcgut.notethunder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jcgut on 02/27/2018.
 */

public class MemoDatabaseAccess {
    private SQLiteDatabase database;
    private MemoDatabaseOpenHelper openHelper;
    private static volatile MemoDatabaseAccess instance;

    private MemoDatabaseAccess (Context context){
        this.openHelper = new MemoDatabaseOpenHelper(context);
    }

    public static synchronized MemoDatabaseAccess getInstance(Context context){
        if(instance == null){
            instance = new MemoDatabaseAccess( context);
        }
        return instance;
    }

    public void open(){
        this.database = openHelper.getWritableDatabase();
    }

    public void close(){
        if (database != null){
            this.database.close();
        }
    }

    public void save(MemoSerialize memoSerialize){
        ContentValues values = new ContentValues();
        values.put("date",memoSerialize.getTime());
        values.put("memo",memoSerialize.getText());
        String date = Long.toString(memoSerialize.getTime());
        database.update(MemoDatabaseOpenHelper.TABLE,values,"date = ?",new String[]{date});
    }

    public void update(MemoSerialize memoSerialize){
        ContentValues values = new ContentValues();
        values.put("date", new Date().getTime());
        values.put("memo", memoSerialize.getText());
        String date = Long.toString(memoSerialize.getTime());
        database.update(MemoDatabaseOpenHelper.TABLE,values,"date = ?", new String[]{date});
    }

    public void delete(MemoSerialize memoSerialize){
        String date=Long.toString(memoSerialize.getTime());
        database.delete(MemoDatabaseOpenHelper.TABLE,"date = ?", new String[]{date});
    }

    public List getAllMemos(){
        List memos = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Memo ORDER BY date DESC",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Long time = cursor.getLong(0);
            String text = cursor.getString(1);
            memos.add(new MemoSerialize(time,text));
            cursor.moveToNext();
        }
        cursor.close();
        return memos;
    }
}
