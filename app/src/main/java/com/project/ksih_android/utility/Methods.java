package com.project.ksih_android.utility;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private static boolean isAdmin;
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

    public static boolean checkAdmin(String uId) {
        ;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("admin").child("uId");
        Timber.d("reference: %s", reference.toString());
        reference.addValueEventListener(new ValueEventListener() {

            private String adminId;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminId = dataSnapshot.getValue(String.class);
                Timber.d("reference: %s", adminId);
                isAdmin = uId.equals(adminId);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return isAdmin;
    }
}
