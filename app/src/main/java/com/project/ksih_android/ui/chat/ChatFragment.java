package com.project.ksih_android.ui.chat;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.project.ksih_android.R;
import com.project.ksih_android.ui.chat.adapters.TabsPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    //Initialize variables
    public ViewPager mViewPager;
    private TabsPagerAdapter mTabsPagerAdapter;
//    public ConnectivityReceiver connectivityReceiver;
    private TabLayout mTabLayout;

    //firebase utils
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseReference;
    public FirebaseUser currentUser;

    private ChatViewModel chatViewModel;
    public ChatFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chat, container, false);
//        chatViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//            }
//        });

        //check and get current user data
//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
//        if (currentUser != null){
//            String user_uID = mAuth.getCurrentUser().getUid();
//            userDatabaseReference = FirebaseDatabase.getInstance().getReference()
//                    .child("users").child(user_uID);
//        }

        /*
         * Tabs >> Viewpager for Chat Activity
         */
        mViewPager = root.findViewById(R.id.tabs_pager);
        mTabsPagerAdapter = new TabsPagerAdapter(getChildFragmentManager(), 2);
        mViewPager.setAdapter(mTabsPagerAdapter);
        mTabLayout = root.findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        return root;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        currentUser = mAuth.getCurrentUser();
//        if (currentUser == null){
//            Toast.makeText(this, "Login to use chat session", Toast.LENGTH_SHORT).show();
//        }
//        if (currentUser != null){
//            userDatabaseReference.child("active_now").setValue("true");
//        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        //Register connectivity BroadcastReceiver
//        connectivityReceiver = new ConnectivityReceiver();
//        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        getContext().registerReceiver(connectivityReceiver, intentFilter);
//    }

//    @Override
//    public void onStop() {
//        super.onStop();
//
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (currentUser != null){
//            userDatabaseReference.child("active_now").setValue(ServerValue.TIMESTAMP);
//        }
//    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.main_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()){
//            case R.id.menu_search:
//                return true;
//            case R.id.profile_settings:
//                return true;
//            case R.id.all_friends:
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    public class ConnectivityReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
//                    Context.CONNECTIVITY_SERVICE);
//            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//            if (networkInfo != null && networkInfo.isConnected()){
//
//            }else {
//                Snackbar snackbar = Snackbar
//                        .make(mViewPager, "No internet Connection! ", Snackbar.LENGTH_LONG)
//                        .setAction("Go settings", view -> {
//                            Intent settings = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
//                            context.startActivity(settings);
//                        });
//                // customizing snackbar
//                snackbar.setActionTextColor(Color.BLACK);
//                View view = snackbar.getView();
//                view.setBackgroundColor(ContextCompat.getColor(context, R.color.button_color_disabled));
//                snackbar.setText("Check network");
//                snackbar.setTextColor(Color.WHITE);
//                snackbar.show();
//            }
//        }
//    }


