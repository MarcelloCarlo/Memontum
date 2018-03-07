package com.example.jcgut.notethunder;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class LockScreenMemontumService extends Service implements View.OnClickListener {


    private LinearLayout linearLayout;
    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;
    private MemoSerialize memoSerialize;
    Button btnCancel,btnSave;
    EditText txtNote;


    @Override
    public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, intentFilter);
        windowManager = ((WindowManager) getSystemService(WINDOW_SERVICE));
        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN //draw on status bar
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION,// hiding the home screen button
                PixelFormat.TRANSPARENT);
    }

    private void init() {
        linearLayout = new LinearLayout(this);
        windowManager.addView(linearLayout, layoutParams);
        ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.memofield_layout, linearLayout);
        btnCancel = linearLayout.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        btnSave = linearLayout.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        txtNote = linearLayout.findViewById(R.id.txtNote);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnCancel:
                windowManager.removeView(linearLayout);
                linearLayout = null;
                break;
            case R.id.btnSave:
                MemoDatabaseAccess databaseAccess = MemoDatabaseAccess.getInstance(this);
                databaseAccess.open();
                if(memoSerialize == null) {
                    //Add memo
                    MemoSerialize tmp = new MemoSerialize();
                    tmp.setText((txtNote.getText().toString()));
                    databaseAccess.save(tmp);
                } else {
                    memoSerialize.setText((txtNote.getText().toString()));
                    databaseAccess.update(memoSerialize);
                }
                databaseAccess.close();
                break;
            default:
                break;
        }

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(screenReceiver);
        super.onDestroy();
    }

    BroadcastReceiver screenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) && linearLayout == null) {
                init();
            }
        }
    };


}
