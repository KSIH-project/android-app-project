package com.project.ksih_android.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.project.ksih_android.R;
import com.project.ksih_android.ui.drawer.DividerItemDecoration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "HomeActivity";
    private NavController mNavController;
    private Toolbar toolBar;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawer = findViewById(R.id.drawer_layout);
        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navigationView = findViewById(R.id.nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(this));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolBar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_signIn, R.id.navigation_project, R.id.navigation_member,
                R.id.navigation_startup, R.id.navigation_event, R.id.nav_chats, R.id.nav_terms)
                .setDrawerLayout(drawer)
                .build();

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, mNavController);
        initDestinationListener();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return mNavController.navigateUp() || super.onSupportNavigateUp();
    }

    // Use this to alter the visibility of the action bar and the bottom navigation bar
    private void initDestinationListener() {
        mNavController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                    @Override
                    public void onDestinationChanged(@NonNull NavController controller,
                                                     @NonNull NavDestination destination, @Nullable Bundle arguments) {
                        try {
                            String dest = getResources().getResourceName(destination.getId());
                            Log.d(TAG, "onDestinationChanged: " + dest);
                        } catch (Resources.NotFoundException e) {
                            destination.getId();
                        }
                        switch (destination.getId()) {
                            case R.id.onBoardingFragment:
                                hideCustomToolBar();
                                hideDrawer();
                                break;
                            default:
                                showCustomToolBar();
                                showDrawer();
                        }
                    }
                });
    }
    private void hideCustomToolBar(){
        toolBar.setVisibility(View.INVISIBLE);
    }
    private void showCustomToolBar(){
        toolBar.setVisibility(View.VISIBLE);
    }

    private void hideDrawer() {
        drawer.setVisibility(View.INVISIBLE);
    }

    private void showDrawer() {
        drawer.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_signIn:
                mNavController.navigate(R.id.nav_signIn);
        }
        return false;
    }
}
