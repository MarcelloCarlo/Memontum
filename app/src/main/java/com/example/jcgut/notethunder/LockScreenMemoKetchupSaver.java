package com.example.jcgut.notethunder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class LockScreenMemoKetchupSaver extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private class MemoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context uhnnn, Intent yeahhhh) {
            String title = yeahhhh.getStringExtra("Title");
        }
    }
}
