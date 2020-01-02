package com.project.ksih_android.ui.chat.chatHome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toolbar;

import com.project.ksih_android.R;
import com.project.ksih_android.ui.chat.adapters.TabsPagerAdapter;

public class ChatActivity extends AppCompatActivity {
    //Initialize variables
    private static final int TIME_LIMIT = 1500;
    private static long backPressed;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabsPagerAdapter mTabsPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}
