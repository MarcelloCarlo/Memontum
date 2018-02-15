package com.example.jcgut.notethunder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
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

public class LockScreenNoteThunderActivity extends Activity {


    /*public LockScreenNoteThunderService() {
    }*/

    private BroadcastReceiver aReceiver;
    private boolean ShowingBa = false;

    private WindowManager windowManager;
    private EditText txtInsertNote;
    private TextView lblNoteThunder;
    private LinearLayout notePadLayout;
    LayoutInflater notePadInflater;
    View notePadView;
    WindowManager.LayoutParams params,paramsLayout,paramsManual;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        //Adding Layout to test TYPE_APPLICATION_LAYOUT Hanash
        notePadLayout = new LinearLayout(this);
        notePadLayout.setOrientation(LinearLayout.VERTICAL);

        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        notePadInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        notePadView = notePadInflater.inflate(R.layout.notepad_layout,null);

        //Adding Layout to test TYPE_APPLICATION_LAYOUT Hanash
      /*  notePadLayout = new LinearLayout(this);
        notePadLayout.setOrientation(LinearLayout.VERTICAL);*/

        //Adding textview and edittext for fun
        lblNoteThunder = new TextView(this);
        lblNoteThunder.setText("Tite");
        lblNoteThunder.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        lblNoteThunder.setTextSize(28f);
        txtInsertNote = new EditText(this);
        txtInsertNote.setTextColor(ContextCompat.getColor(this,android.R.color.white));
        txtInsertNote.setTextSize(24f);

        //Set parameters for both controls (Create a separate param if this doesn't work for both)
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.BOTTOM;

        paramsLayout = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,/* WindowManager.LayoutParams.TYPE_APPLICATION_PANEL */ WindowManager.LayoutParams.TYPE_PHONE ,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);
        paramsLayout.gravity = Gravity.BOTTOM;

        paramsManual = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        paramsManual.gravity = Gravity.CENTER;
        paramsManual.x=0;
        paramsManual.y=0;

        windowManager.addView(notePadView,paramsManual);

        //Register Receiver
        aReceiver = new LockScreenNoteThunderActivity.LockScreenStateReceiver();
        IntentFilter afilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        afilter.addAction(Intent.ACTION_USER_PRESENT);

        registerReceiver(aReceiver, afilter);
    }

    public class LockScreenStateReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context ctx, Intent intent){
            if (Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_OFF)){
                //if the thing is off then it will show
                if (!ShowingBa){

                   /* windowManager.addView(lblNoteThunder, params);
                    windowManager.addView(txtInsertNote, params);*/
                    windowManager.addView(notePadLayout,paramsLayout);
                    ShowingBa = true;
                }
            }
            else if(Objects.equals(intent.getAction(), Intent.ACTION_USER_PRESENT)){

                //handle good stuff if the screen is unlocked
                if(ShowingBa){
                   /* windowManager.removeView(lblNoteThunder);
                    windowManager.removeView(txtInsertNote);*/
                    windowManager.removeView(notePadLayout);
                    ShowingBa = false;
                }
            }
        }
    }

    @Override
    public void onDestroy(){
        //unregister receiver when the service is destroy unless if you want some complications, you  can modify it
        if(aReceiver != null){
            unregisterReceiver(aReceiver);
        }

        //it removes the view id the service is destroyed
        if(ShowingBa){
           /* windowManager.removeView(lblNoteThunder);
            windowManager.removeView(txtInsertNote);*/
            windowManager.removeView(notePadLayout);
            ShowingBa = false;
        }
        super.onDestroy();
    }
}
