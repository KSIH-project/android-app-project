package com.project.ksih_android.utility;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import timber.log.Timber;


public class UrlChecker extends AsyncTask<Void, Void, Boolean> {
    private String mUrl;

    public UrlChecker(String mUrl) {
        this.mUrl = mUrl;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            URL url = new URL(mUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                Timber.d("connection Successful : %d", urlConnection.getResponseCode());
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }
}
