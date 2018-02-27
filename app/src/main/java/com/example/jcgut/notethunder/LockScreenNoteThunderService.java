package com.example.jcgut.notethunder;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

public class LockScreenNoteThunderService extends Service {
    /*public LockScreenNoteThunderService() {
    }*/

    LayoutInflater notePadInflater;
    View notePadView;
    WindowManager.LayoutParams params,paramsLayout,paramsManual;
    private BroadcastReceiver aReceiver;
    private boolean ShowingBa = false;
    private WindowManager windowManager;
    private EditText txtInsertNote;
    private TextView lblNoteThunder;
    private LinearLayout notePadLayout;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        //Adding Layout to test TYPE_APPLICATION_LAYOUT Hanash
        notePadLayout = new LinearLayout(this);
        notePadLayout.setOrientation(LinearLayout.VERTICAL);

        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        notePadInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        notePadView = notePadInflater.inflate(R.layout.memofield_layout,null);

        //Adding textview and edittext for fun
        lblNoteThunder = new TextView(this);
        lblNoteThunder.setText("Tite");
        lblNoteThunder.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        lblNoteThunder.setTextSize(40f);
        txtInsertNote = new EditText(this);
        txtInsertNote.setTextColor(ContextCompat.getColor(this,android.R.color.white));
        txtInsertNote.setTextSize(24f);

        //Set parameters for both controls (Create a separate param if this doesn't work for both)
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER;

        paramsLayout = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,/* WindowManager.LayoutParams.TYPE_APPLICATION_PANEL */ WindowManager.LayoutParams.TYPE_PHONE ,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.OPAQUE);
        paramsLayout.gravity = Gravity.CENTER;

        paramsManual = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                PixelFormat.TRANSPARENT);
        paramsManual.gravity = Gravity.CENTER;
        paramsManual.x=0;
        paramsManual.y=0;

//        windowManager.addView(notePadView,paramsManual);

        //Register Receiver
        aReceiver = new LockScreenStateReceiver();
        IntentFilter afilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        afilter.addAction(Intent.ACTION_USER_PRESENT);

        registerReceiver(aReceiver, afilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return  START_STICKY;
    }

    @Override
    public void onDestroy(){
        //unregister receiver when the service is destroy unless if you want some complications, you  can modify it
        if(aReceiver != null){
            unregisterReceiver(aReceiver);
        }

        //it removes the view id the service is destroyed
        if(ShowingBa){
            /*windowManager.removeView(lblNoteThunder);*/
            /*windowManager.removeView(txtInsertNote);*/
           windowManager.removeView(notePadView);
            ShowingBa = false;
        }
        super.onDestroy();
    }

    public class LockScreenStateReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context ctx, Intent intent){
            if (Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_OFF)){
                //if the thing is off then it will show
                if (!ShowingBa){

                   /* windowManager.addView(lblNoteThunder, params);*/
                    /*windowManager.addView(txtInsertNote, params);*/
                   windowManager.addView(notePadView,paramsManual);
                    ShowingBa = true;
                }
            }
            else if(Objects.equals(intent.getAction(), Intent.ACTION_USER_PRESENT)){

                //handle good stuff if the screen is unlocked
                if(ShowingBa){
                   /* windowManager.removeView(lblNoteThunder);*/
                   /* windowManager.removeView(txtInsertNote);*/
                   windowManager.removeView(notePadView);
                    ShowingBa = false;
                }
            }
        }
    }
}
