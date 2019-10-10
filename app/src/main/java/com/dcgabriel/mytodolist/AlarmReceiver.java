package com.dcgabriel.mytodolist;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String todoTitle = intent.getStringExtra(MainActivity.TODO_TITLE);
        String todoDesc = intent.getStringExtra(MainActivity.TODO_DESC);
        int todoIsComplete = intent.getIntExtra(MainActivity.TODO_IS_COMPLETE, 0);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, PendingIntent.FLAG_ONE_SHOT);

        Toast.makeText(context,todoTitle,Toast.LENGTH_SHORT).show();

        Log.d(TAG, "***********************************************************onReceive: " + todoTitle + " " + todoDesc);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);


        String NOTIFICATION_CHANNEL_ID = "101";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(todoTitle)
                .setContentText(todoDesc)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setContentIntent(pendingIntent)
                .setContentInfo("Info")

                .build();

        int id = generateID();
        Log.d(TAG, "***********************onReceive: " + id);
        notificationManager.notify(id, notification);
    }

    private int generateID(){

        int id = (int) System.currentTimeMillis();
        return id;
    }
}
