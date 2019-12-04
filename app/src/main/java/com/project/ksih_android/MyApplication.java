package com.project.ksih_android;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by SegunFrancis
 */

/*
* This is not the MainActivity class
*  This class is for Logging with Timber
**/

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }
    }
}
