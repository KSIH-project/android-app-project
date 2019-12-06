package com.project.ksih_android.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.ksih_android.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView mNavView;
    private NavController mNavController;

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
    }

    @Override
    public boolean onSupportNavigateUp() {
        return mNavController.navigateUp() || super.onSupportNavigateUp();
    }

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
                                break;
                            default:
                                showBottomNavBar();
                                showToolbar();
                        }
                    }
                });
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
}
