package com.example.jcgut.notethunder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jcgut on 02/27/2018.
 */

public class MemoDatabaseAccess {
    SQLiteDatabase database;
    MemoDatabaseOpenHelper openHelper;
    private static volatile MemoDatabaseAccess instance;

    public MemoDatabaseAccess (Context context){
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
        values.put("dates",memoSerialize.getTime());
        values.put("memo",memoSerialize.getText());
        String date = Long.toString(memoSerialize.getTime());
        database.update(MemoDatabaseOpenHelper.TABLE,values,"dates = ?",new String[]{date});
    }

    public void update(MemoSerialize memoSerialize){
        ContentValues values = new ContentValues();
        values.put("dates", new Date().getTime());
        values.put("memo", memoSerialize.getText());
        String date = Long.toString(memoSerialize.getTime());
        database.update(MemoDatabaseOpenHelper.TABLE,values,"dates = ?", new String[]{date});
    }

    public void delete(MemoSerialize memoSerialize){
        String date=Long.toString(memoSerialize.getTime());
        database.delete(MemoDatabaseOpenHelper.TABLE,"dates = ?", new String[]{date});
    }

    public List<MemoSerialize> getAllMemos(){
        List<MemoSerialize> memosModel = new ArrayList<>();
        SQLiteDatabase memoDB = openHelper.getReadableDatabase();
        String[] columns = {"date","memo"};
        String querySql = "SELECT * FROM Memos ORDER BY dates DESC";
         Cursor cursor = memoDB.rawQuery("SELECT * FROM Memos ORDER BY dates",null);
        if (cursor == null){
            Toast.makeText(null,"OhSnap",Toast.LENGTH_SHORT).show();
        }

       /* cursor = database.query("Memos",columns,null,null,null,null,null);*/
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Long time = cursor.getLong(0);
            String text = cursor.getString(1);
            memosModel.add(new MemoSerialize(time,text));
            cursor.moveToNext();
        }
        cursor.close();
        return memosModel;
    }
}
