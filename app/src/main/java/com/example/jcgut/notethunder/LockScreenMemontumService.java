package com.example.jcgut.notethunder;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jcgut.notethunder.domain.Memo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class LockScreenMemontumService extends Service {

    private LinearLayout linearLayout;
    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;
    FloatingActionButton btnCancel;
    RecyclerView recyclerView;
    Context ctx;
    List<Memo> data = new ArrayList<>();
    List<Memo> arrayMemo;
    View view;
    LayoutInflater li;
    ListAdapter listAdapter;

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
        li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        windowManager = ((WindowManager) getSystemService(WINDOW_SERVICE));
        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION, //hiding the home screen button
                PixelFormat.TRANSPARENT);
    }


    private void init(Context ctx) {
        linearLayout = new LinearLayout(this);
        windowManager.addView(linearLayout, layoutParams);
        view = li.inflate(R.layout.lockscreen_layout, linearLayout);
        this.ctx = ctx;
        recyclerView = view.findViewById(R.id.memorecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        try {
            listAdapter = new ListAdapter(ctx,data);
            recyclerView.setAdapter(listAdapter);
        }catch (Exception x) {x.printStackTrace();}

        btnCancel = view.findViewById(R.id.btnCancelScreen);
        btnCancel.setOnClickListener(listener);

    }

    View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.btnCancelScreen:
                    windowManager.removeView(linearLayout);
                    linearLayout = null;
                    break;
                default:
                    break;
            }

        }

    };

    @Override
    public void onDestroy() {
        unregisterReceiver(screenReceiver);
        super.onDestroy();
    }

    BroadcastReceiver screenReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) && linearLayout == null) {
                init(context);

            }
        }
    };


}
