package com.halabang.kewpm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import 	android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by Joey on 8/10/2016.
 */
public class MyBroadCastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){
        Intent service = new Intent(context, MyFirebaseMessageService.class);
        context.startService(service);
        //createNotification(context);
        //Toast.makeText(context, "Broadcast started", Toast.LENGTH_LONG).show();
    }
}

