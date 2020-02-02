package com.project.ksih_android.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.project.ksih_android.data.Events;
import com.project.ksih_android.utility.Constants;

/**
 * Created by SegunFrancis
 */

public class SharedPreferencesStorage {

    private SharedPreferences mPreferences;

    public SharedPreferencesStorage(Context context) {
        mPreferences = context.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE);
    }

    public void setUserName(String key, String value) {
        SharedPreferences.Editor pref = mPreferences.edit();
        pref.putString(key, value);
        pref.apply();
    }

    public void setUserPassword(String key, String value) {
        SharedPreferences.Editor pref = mPreferences.edit();
        pref.putString(key, value);
        pref.apply();
    }

    public void setSeenOnBoardingScreen(String key, boolean value) {
        SharedPreferences.Editor pref = mPreferences.edit();
        pref.putBoolean(key, value);
        pref.apply();
    }

    public void setRememberMe(String key, boolean value) {
        SharedPreferences.Editor pref = mPreferences.edit();
        pref.putBoolean(key, value);
        pref.apply();
    }

    public String getUserName(String key) {
        return mPreferences.getString(key, "");
    }

    public String getUserPassword(String key) {
        return mPreferences.getString(key, "");
    }

    public boolean hasSeenOnBoardingScreen(String key) {
        return mPreferences.getBoolean(key, false);
    }

    public boolean isRememberMeChecked(String key) {
        return mPreferences.getBoolean(key, false);
    }

}
