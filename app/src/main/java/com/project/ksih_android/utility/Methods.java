package com.project.ksih_android.utility;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import timber.log.Timber;

/**
 * Created by SegunFrancis
 */
public class Methods {

    /**
     * This method is used to hide the soft keyboard
     *
     * @param activity The current activity in view
     */

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            view = new View(activity);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isUrlValid(String url) {
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... strings) {
                try {
                    URL url1 = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                    int status = connection.getResponseCode();
                    Timber.d("response: %d", status);
                    if (HttpURLConnection.HTTP_OK == status) {

                        Timber.d("response: %d", status);
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Timber.d("catch: %s", e.getLocalizedMessage());
                }
                return false;
            }
        };
        task.execute();
        return false;
    }

}

