package com.project.ksih_android.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.project.ksih_android.data.StartUpField;
import com.project.ksih_android.utility.Constants;

/**
 * Created by SegunFrancis
 */

public class SharedPreferencesStorage {

    private SharedPreferences mPreferences;

    public SharedPreferencesStorage(Context context) {
        mPreferences = context.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE);
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

    public String getUserEmail(String key) {
        return mPreferences.getString(key, "");
    }

    public String getProfilePhotoUrl(String key) {
        return mPreferences.getString(key, "");
    }

    public boolean hasSeenOnBoardingScreen(String key) {
        return mPreferences.getBoolean(key, false);
    }

    public boolean isRememberMeChecked(String key) {
        return mPreferences.getBoolean(key, false);
    }


    public void setUserName(String key, String username) {
        SharedPreferences.Editor pref = mPreferences.edit();
        pref.putString(key, username);
        pref.apply();
    }

    public void setUserEmail(String key, String email) {
        SharedPreferences.Editor pref = mPreferences.edit();
        pref.putString(key, email);
        pref.apply();
    }

    public void setProfilePhotoUrl(String key, String photoUrl) {
        SharedPreferences.Editor pref = mPreferences.edit();
        pref.putString(key, photoUrl);
        pref.apply();
    }

    public void setStartupField(String key, StartUpField startupField) {
        SharedPreferences.Editor pref = mPreferences.edit();
        Gson gson = new Gson();
        String field = gson.toJson(startupField);
        pref.putString(key, field);
        pref.apply();
    }

    public StartUpField getStartUpField(String key) {
        Gson gson = new Gson();
        String field = mPreferences.getString(key, "");
        return gson.fromJson(field, StartUpField.class);
    }
}
