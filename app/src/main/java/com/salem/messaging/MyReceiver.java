package com.salem.messaging;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.telephony.SmsManager;
import android.widget.Toast;

import static android.content.Intent.getIntent;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl=pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"");

        for (int j = 0; j < MainActivity3.results.size(); j++) {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(MainActivity3.results.get(j).getPhoneNumbers().get(0).getNumber(), null, EditScheduled.x, null, null);
        }

        wl.release();
    }

    public void sendMessage(String number,String txt,String name){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, txt, null, null);
    }
}
