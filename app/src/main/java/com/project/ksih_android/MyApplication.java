package com.project.ksih_android;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.project.ksih_android.storage.SharedPreferencesStorage;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

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

    //adding variables
    private DatabaseReference userDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentOnlineUser;

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

        // all images >> load offline
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso builtPicasso = builder.build();
        builtPicasso.setIndicatorsEnabled(true);
        builtPicasso.setLoggingEnabled(true);
        Picasso.setSingletonInstance(builtPicasso);

        //Online Status
        mAuth = FirebaseAuth.getInstance();
        currentOnlineUser = mAuth.getCurrentUser();
        if (currentOnlineUser != null){
            String user_uID = mAuth.getCurrentUser().getUid();
            userDatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(user_uID);

            userDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    userDatabaseReference.child("active_now").onDisconnect().setValue(ServerValue.TIMESTAMP);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
