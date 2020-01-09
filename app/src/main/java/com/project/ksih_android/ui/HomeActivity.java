package com.project.ksih_android.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.project.ksih_android.R;
import com.project.ksih_android.utility.DividerItemDecoration;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import timber.log.Timber;

public class HomeActivity extends AppCompatActivity {

    private NavController mNavController;
    private Toolbar toolBar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawer = findViewById(R.id.drawer_layout);
        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavigationView navigationView = findViewById(R.id.nav_drawer);
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(this));

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolBar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_signIn, R.id.navigation_project, R.id.navigation_member,
                R.id.navigation_startup, R.id.navigation_event, R.id.nav_chats, R.id.nav_settings, R.id.ksih_rules)
                .setDrawerLayout(drawer)
                .build();

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, mNavController);
        initDestinationListener();
    }

    @Override
    public boolean onSupportNavigateUp() {
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(mNavController, drawer);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if (mNavController.getCurrentDestination().getId() == R.id.navigation_project)
            showDialog();
        else
            mNavController.navigateUp();
    }

    // Use this to alter the visibility of the action bar and the toolbar bar
    private void initDestinationListener() {
        mNavController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            try {
                String dest = getResources().getResourceName(destination.getId());
                Timber.d("onDestinationChanged: " + dest);
            } catch (Resources.NotFoundException e) {
                destination.getId();
            }
            switch (destination.getId()) {
                case R.id.eventAddFragment:
                case R.id.eventDetailsFragment:
                    lockDrawer();
                    break;
                case R.id.onBoardingFragment:
                    hideCustomToolBar();
                    hideDrawer();
                    break;
                case R.id.all_friends:
                case R.id.menu_search:
                    hideCustomToolBar();
                    getSupportActionBar().hide();
                    break;
                default:
                    showCustomToolBar();
                    showDrawer();
            }
        });
    }

    private void showDialog() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(HomeActivity.this);
        dialog.setMessage("Are you sure you want to exit?")
                .setPositiveButton("YES", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    System.exit(0);
                })
                .setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.dismiss());
        dialog.create().show();
    }

    private void hideCustomToolBar() {
        toolBar.setVisibility(View.INVISIBLE);
    }

    private void showCustomToolBar() {
        toolBar.setVisibility(View.VISIBLE);
    }

    private void hideDrawer() {
        drawer.setVisibility(View.INVISIBLE);
    }

    private void lockDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void showDrawer() {
        drawer.setVisibility(View.VISIBLE);
    }
}
