package com.example.jcgut.notethunder;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jcgut.notethunder.cutomEdittext.LinedEdittext;
import com.example.jcgut.notethunder.data.DBHelper;
import com.example.jcgut.notethunder.domain.Memo;
import com.example.jcgut.notethunder.interfaces.DetailInterface;
import com.example.jcgut.notethunder.interfaces.ListInterface;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static com.example.jcgut.notethunder.MainActivity.REQ_CAMERA;
import static com.example.jcgut.notethunder.MainActivity.REQ_GALLERY;

public class LockScreenMemontumActivity extends Activity {

    private LinearLayout linearLayout;
    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;
    FloatingActionButton btnCancel;
    RecyclerView recyclerView;
    ListAdapter listAdapter;
    Context ctx;
    List<Memo> data = new ArrayList<>();
    List<Memo> arrayMemo;
    View view;
    LayoutInflater li;
    ListInterface listMemoIntr;
    MemoWrapper memoWrapper;

    @Override
    public void onCreateView() {
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

      /*  DBHelper mdbHelper = new DBHelper(getApplicationContext());
        Dao<Memo, Long> memoDao = null;
        try {
            memoDao = mdbHelper.getMemoDao();
            arrayMemo = memoDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }


    private void init(Context ctx) {
        linearLayout = new LinearLayout(this);
        windowManager.addView(linearLayout, layoutParams);
        view = li.inflate(R.layout.lockscreen_layout, linearLayout);
        this.ctx = ctx;
        recyclerView = view.findViewById(R.id.memorecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        try {
            recyclerView.setAdapter(new ListAdapter(getApplicationContext(),arrayMemo));
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

    public void setLockedData(List<Memo> datas) {
        this.data = datas;
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
                init(context);

            }
        }
    };


}
