package com.project.ksih_android.utility;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.project.ksih_android.R;
import com.project.ksih_android.ui.HomeActivity;
import com.project.ksih_android.utility.notification.Token;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    NotificationManager notificationManager;
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        if (firebaseUser != null){
            updateToken(refreshToken);
        }
    }

    private void updateToken(String refreshToken) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(refreshToken);
        reference.child(firebaseUser.getUid()).setValue(token);
    }

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
            notificationChannel.setVibrationPattern(null);
            notificationChannel.enableVibration(false);
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

        //clicking notification goes to sender chat
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("visitUserId", from_sender_id);


        PendingIntent pendingIntent1 = new  NavDeepLinkBuilder(this)
                .setComponentName(HomeActivity.class)
                .setGraph(R.navigation.mobile_navigation)
                .setDestination(R.id.nav_chats)
                .createPendingIntent();


        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        notificationBuilder.setContentIntent(pendingIntent1);

        int mNotificationId = (int) System.currentTimeMillis();
        notificationManager.notify(mNotificationId, notificationBuilder.build());

    }
}
