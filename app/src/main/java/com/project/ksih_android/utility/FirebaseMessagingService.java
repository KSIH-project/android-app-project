package com.project.ksih_android.utility;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.project.ksih_android.R;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "ksih_channel_1";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "ksih Notification", NotificationManager.IMPORTANCE_HIGH);

            //configure notification channel
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        //retrieve title and notification body from index.js function through remote
        String from_sender_id = remoteMessage.getData().get("from_sender_id");
        String click_action = remoteMessage.getData().get("click_action");
        String notificationTitle = remoteMessage.getData().get("title");
        String notificationBody = remoteMessage.getData().get("body");

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ksih_background)
                .setTicker("infbox.com.bd")
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody);

        //clicking notification goes to sender profile
        Intent intent = new Intent(click_action);
        intent.putExtra("visitUserId", from_sender_id);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        notificationBuilder.setContentIntent(pendingIntent);

        int mNotificationId = (int) System.currentTimeMillis();
        notificationManager.notify(mNotificationId, notificationBuilder.build());

    }
}
