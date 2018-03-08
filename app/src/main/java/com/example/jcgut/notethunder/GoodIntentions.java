package com.example.jcgut.notethunder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jcgut on 3/8/2018.
 */

public class GoodIntentions extends BroadcastReceiver {
    public GoodIntentions() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //context.startService(new Intent(context, LockScreenMemontumService.class));
        context.startActivity(new Intent(context, LockScreenMomentumActivity.class));
    }
}
