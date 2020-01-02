package com.project.ksih_android.ui.chat.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.project.ksih_android.R;

public class ConnectivityReceiver extends BroadcastReceiver {
    View view;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){

        }else {
            Snackbar snackbar = Snackbar
                    .make(view, "No internet Connection! ", Snackbar.LENGTH_LONG)
                    .setAction("Go settings", view -> {
                        Intent settings = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(settings);
                    });
            // customizing snackbar
            snackbar.setActionTextColor(Color.BLACK);
            View view = snackbar.getView();
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            snackbar.setText("Go Settingd");
            snackbar.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }
}