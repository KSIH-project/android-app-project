package com.project.ksih_android.ui.drawer;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.project.ksih_android.R;
import com.project.ksih_android.ui.HomeActivity;

public class DrawerItemActivity extends HomeActivity {
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_item_layout);

        mNavController = Navigation.findNavController(this, R.id.nav_drawer_fragment);
    }

}
