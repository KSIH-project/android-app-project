package com.project.ksih_android.ui.chat.chatHome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ksih_android.R;
import com.project.ksih_android.ui.chat.adapters.TabsPagerAdapter;

public class ChatActivity extends AppCompatActivity {
    //Initialize variables
    private static final int TIME_LIMIT = 1500;
    private static long backPressed;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabsPagerAdapter mTabsPagerAdapter;
    private ConnectivityManager connectivityReceiver;
    private TabLayout mTabLayout;

    //firebase utils
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseReference;
    public FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //check and get current user data
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            String user_uID = mAuth.getCurrentUser().getUid();
            userDatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(user_uID);
        }

        /*
         * Tabs >> Viewpager for Chat Activity
         */
        mViewPager = findViewById(R.id.tabs_pager);
        mTabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), 2);
        mViewPager.setAdapter(mTabsPagerAdapter);

        mTabLayout = findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
