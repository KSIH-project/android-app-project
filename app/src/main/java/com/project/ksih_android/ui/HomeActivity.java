package com.project.ksih_android.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;
import com.project.ksih_android.storage.SharedPreferencesStorage;
import com.project.ksih_android.ui.sharedViewModel.SharedViewModel;
import com.project.ksih_android.utility.DividerItemDecoration;
import com.project.ksih_android.utility.Methods;

import timber.log.Timber;

import static com.project.ksih_android.utility.Constants.EMAIL_KEY;
import static com.project.ksih_android.utility.Constants.PROFILE_PHOTO_KEY;
import static com.project.ksih_android.utility.Constants.USERNAME_KEY;

public class HomeActivity extends AppCompatActivity {

    private NavController mNavController;
    private MaterialToolbar toolBar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawer = findViewById(R.id.drawer_layout);
        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // isFirstTimeLogin();
        NavigationView navigationView = findViewById(R.id.nav_drawer);
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(this));

        mToggle = new ActionBarDrawerToggle(
                this, drawer, toolBar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
              Methods.hideSoftKeyboard(HomeActivity.this);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Methods.hideSoftKeyboard(HomeActivity.this);
            }
        };
        drawer.addDrawerListener(mToggle);
        mToggle.syncState();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.loginFragment, R.id.navigation_project, R.id.navigation_member,
                R.id.navigation_startup, R.id.navigation_event, R.id.nav_chats, R.id.nav_settings, R.id.ksih_rules)
                .setDrawerLayout(drawer)
                .build();

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, mNavController);
        initDestinationListener();

        // Inflate navigation view
        View header = navigationView.getHeaderView(0);
        RoundedImageView imageView = header.findViewById(R.id.profile_image);
        TextView profileName = header.findViewById(R.id.profile_name);
        TextView profileEmail = header.findViewById(R.id.profile_email);

        // Get user details from shared preference
        // There is no need to make another database call
        SharedViewModel viewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        SharedPreferencesStorage pref = new SharedPreferencesStorage(this);
        Glide.with(this)
                .load(pref.getProfilePhotoUrl(PROFILE_PHOTO_KEY))
                .placeholder(R.drawable.ic_profile_photo)
                .error(R.drawable.ic_profile_photo)
                .into(imageView);
        profileName.setText(pref.getUserName(USERNAME_KEY));
        profileEmail.setText(pref.getUserEmail(EMAIL_KEY));

        // Read updated data from view model without changing device configuration state
        viewModel.username.observe(this, profileName::setText);
        viewModel.userEmail.observe(this, profileEmail::setText);
        viewModel.userProfilePhotoUrl.observe(this, s -> Glide.with(HomeActivity.this)
                .load(s)
                .placeholder(R.drawable.ic_profile_photo)
                .error(R.drawable.ic_profile_photo)
                .into(imageView));

        // Navigate user to profile screen when user clicks the nav header
        imageView.setOnClickListener(view -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                Navigation.findNavController(HomeActivity.this, R.id.nav_host_fragment).navigate(R.id.profileFragment);
                drawer.closeDrawers();
            } else {
                Toast.makeText(this, "Sign in to view profile", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(HomeActivity.this, R.id.nav_host_fragment).navigate(R.id.loginFragment);
                drawer.closeDrawers();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return mNavController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if (mNavController.getCurrentDestination().getId() == R.id.navigation_member)
            showDialog();
        else
            mNavController.navigateUp();
    }

    // Use this to alter the visibility of the action bar and the toolbar bar
    private void initDestinationListener() {
        mNavController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            try {
                String dest = getResources().getResourceName(destination.getId());
                Timber.d("onDestinationChanged: %s", dest);
            } catch (Resources.NotFoundException e) {
                destination.getId();
            }
            switch (destination.getId()) {
                case R.id.onBoardingFragment:
                case R.id.editPhotoFragment:
                case R.id.zoomFragment:
                case R.id.eventAddFragment:
                case R.id.eventDetailsFragment:
                    hideCustomToolBar();
                    disableNavDrawer();
                    break;
                case R.id.forgotPasswordFragment:
                case R.id.registerFragment:
                case R.id.editProfileFragment:
                    disableNavDrawer();
                    break;
                case R.id.profileFragment:
                case R.id.addStartUpFragment:
                case R.id.startUpDetailsFragment:
                case R.id.appInfo:
                case R.id.fragment_ksih_android_team:
                case R.id.aboutKsihFragment:
                case R.id.privacy_policies:
                    hideCustomToolBar();
                    break;
                default:
                    showCustomToolBar();
                    enableNavDrawer();
            }
        });
    }

    private void showDialog() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(HomeActivity.this);
        dialog.setMessage("Are you sure you want to exit?")
                .setTitle("Exit!")
                .setIcon(R.drawable.ksih_background)
                .setPositiveButton("YES", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    System.exit(0);
                })
                .setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.dismiss());
        dialog.create().show();
    }

    private void hideCustomToolBar() {
        toolBar.setVisibility(View.GONE);
    }

    private void showCustomToolBar() {
        toolBar.setVisibility(View.VISIBLE);
    }

    private void enableNavDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mToggle.setDrawerIndicatorEnabled(true);
        mToggle.syncState();
    }

    private void disableNavDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mToggle.setDrawerIndicatorEnabled(false);
        mToggle.syncState();
    }
}
