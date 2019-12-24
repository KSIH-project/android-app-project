package com.project.ksih_android.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView mNavView;
    private NavController mNavController;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mNavView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, appBarConfiguration);
        NavigationUI.setupWithNavController(mNavView, mNavController);
        initDestinationListener();

        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolBar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(this));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
                    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                        try {
                            String dest = getResources().getResourceName(destination.getId());
                        } catch (Resources.NotFoundException e) {
                            destination.getId();
                        }
                        switch (destination.getId()) {
                            case R.id.onBoardingFragment:
                                hideBottomNavBar();
                                hideToolbar();
                                hideCustomToolBar();
                                break;
                            default:
                                showBottomNavBar();
                                showToolbar();
                                showCustomToolBar();
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

    private void hideBottomNavBar() {
        mNavView.setVisibility(View.GONE);
    }

    private void hideToolbar() {
        getSupportActionBar().hide();
    }

    private void showBottomNavBar() {
        mNavView.setVisibility(View.VISIBLE);
    }

    private void showToolbar() {
        getSupportActionBar().show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch(item.getItemId()) {
            case 0:
                return true;
            case 1:
                return true;
        }
        return false;
    }
}
