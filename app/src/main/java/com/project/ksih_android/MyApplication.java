package com.project.ksih_android;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ksih_android.storage.SharedPreferencesStorage;


import timber.log.Timber;

/**
 * Created by SegunFrancis
 */
/*
* This is not the AuthActivity class
*  This class is for Logging with Timber
* and persistence logic
**/

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Init sharedPreference
        new SharedPreferencesStorage(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }
        //firebase persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
