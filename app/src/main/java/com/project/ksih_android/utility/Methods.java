package com.project.ksih_android.utility;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.project.ksih_android.R;

import androidx.browser.customtabs.CustomTabsIntent;

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

    /**
     * This method is used to load web urls with the custom chrome tab
     *
     * @param url     is the url that is to be loaded with the custom tab
     * @param context specifies where this will be used
     */

    public static void loadUrlWithChromeTab(String url, Context context) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        builder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
