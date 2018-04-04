package com.example.jcgut.notethunder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.jcgut.notethunder.domain.Memo;
import com.example.jcgut.notethunder.interfaces.DetailInterface;

import java.sql.SQLException;
import java.util.Date;

public class LockScreenMemoKetchupSaver extends Activity {

    String title = "";
    String content = "";
    Date newTime;
    DetailInterface detailInterface = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private class MemoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context uhnnn, Intent yeahhhh) {

            title = yeahhhh.getStringExtra("Title");
            content = yeahhhh.getStringExtra("Memo");
            newTime = new Date(System.currentTimeMillis());
            try {
                detailInterface.saveToList(makeMemo());
            } catch (SQLException sqlx){
                sqlx.printStackTrace();
            }


        }
    }

    private Memo makeMemo(){
        Memo memo = new Memo();
        memo.setTitle(title);
        memo.setMemo(content);
        memo.setDate(newTime);
        return memo;
    }
}