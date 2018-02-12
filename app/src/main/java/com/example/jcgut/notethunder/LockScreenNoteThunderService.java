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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class LockScreenNoteThunderService extends Service {
    /*public LockScreenNoteThunderService() {
    }*/

    private BroadcastReceiver aReceiver;
    private boolean ShowingBa = false;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    private WindowManager windowManager;
    private EditText txtInsertNote;
    private TextView lblNoteThunder;
    WindowManager.LayoutParams params;

    @Override
    public void onCreate(){
        super.onCreate();

        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);

        //Adding textview and edittext for fun
        lblNoteThunder = new TextView(this);
        lblNoteThunder.setText("Tite");
        lblNoteThunder.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        lblNoteThunder.setTextSize(28f);
        txtInsertNote = new EditText(this);
        txtInsertNote.setTextColor(ContextCompat.getColor(this,android.R.color.white));
        txtInsertNote.setTextSize(24f);

        //Set parameters for both controls (Create a separate param if this doesn't work for both)
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.BOTTOM;

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

    public class LockScreenStateReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context ctx, Intent intent){
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                //if the thing is off then it will show
                if (!ShowingBa){
                    windowManager.addView(lblNoteThunder, params);
                    windowManager.addView(txtInsertNote, params);
                    ShowingBa = true;
                }
            }
            else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){

                //handle good stuff if the screen is unlocked
                if(ShowingBa){
                    windowManager.removeView(lblNoteThunder);
                    windowManager.removeView(txtInsertNote);
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
            windowManager.removeView(lblNoteThunder);
            windowManager.removeView(txtInsertNote);
            ShowingBa = false;
        }
        super.onDestroy();
    }
}
