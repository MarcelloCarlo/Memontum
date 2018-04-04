package com.example.jcgut.notethunder;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jcgut.notethunder.cutomEdittext.LinedEdittext;
import com.example.jcgut.notethunder.domain.Memo;
import com.example.jcgut.notethunder.interfaces.DetailInterface;
import com.example.jcgut.notethunder.interfaces.ListInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static com.example.jcgut.notethunder.MainActivity.REQ_CAMERA;
import static com.example.jcgut.notethunder.MainActivity.REQ_GALLERY;

public class LockScreenMemontumService extends Service {

    private LinearLayout linearLayout;
    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;
    FloatingActionButton btnCancel;
    RecyclerView recyclerView;
    ListAdapter listAdapter;
    Context ctx;
    int mColumnCount = 1;
    List<Memo> data = new ArrayList<>();
    View view;
    LayoutInflater li;
    ListInterface listMemoIntr;

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
                //WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN //draw on status bar
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION, //hiding the home screen button
                PixelFormat.TRANSPARENT);
        this.listMemoIntr = (ListInterface)ctx;
    }

    private void init() {
        linearLayout = new LinearLayout(this);

        recyclerView = view.findViewById(R.id.memorecyclerView);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(ctx,mColumnCount));
        }
        listAdapter = new ListAdapter(ctx, data);
        recyclerView.setAdapter(listAdapter);
        view = li.inflate(R.layout.lockscreen_layout, linearLayout);
        btnCancel = view.findViewById(R.id.btnCancelScreen);
        btnCancel.setOnClickListener(listener);

        windowManager.addView(view, layoutParams);

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

    public void setData(List<Memo> MemoData){
        this.data = MemoData;
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
