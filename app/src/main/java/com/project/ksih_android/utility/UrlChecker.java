package com.project.ksih_android.utility;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlChecker extends AsyncTask<String, Void, Boolean> {

    public UrlChecker(String murl) {

    }

    @Override
    protected Boolean doInBackground(String... strings) {

        try {
            URL url = new URL(strings[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();
            return httpURLConnection.getResponseCode() == 200;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
