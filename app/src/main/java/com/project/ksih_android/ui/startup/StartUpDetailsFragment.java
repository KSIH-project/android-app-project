package com.project.ksih_android.ui.startup;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.project.ksih_android.R;
import com.project.ksih_android.storage.SharedPreferencesStorage;

import static com.project.ksih_android.utility.Constants.STARTUP_ITEM_KEY;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * It displays details about a startup
 */
public class StartUpDetailsFragment extends Fragment {

    private StartUpField mField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_start_up_details, container, false);
        SharedPreferencesStorage mStorage = new SharedPreferencesStorage(requireContext());
        mField = mStorage.getStartUpField(STARTUP_ITEM_KEY);

        MaterialToolbar startUpDetailsToolbar = root.findViewById(R.id.startup_details_toolbar);
        ImageView startupIcon = root.findViewById(R.id.startup_detail_logo);
        TextView startupDescriptionET = root.findViewById(R.id.startupDescriptionET);
        TextView startupFounderET = root.findViewById(R.id.startupFounderET);
        TextView startupCoFounderET = root.findViewById(R.id.startupCoFounderET);
        TextView startupTelephoneET = root.findViewById(R.id.startupTelephoneET);
        TextView startupEmailET = root.findViewById(R.id.startupEmailET);
        TextView startupWebsiteET = root.findViewById(R.id.startupWebsiteET);
        TextView facebookET = root.findViewById(R.id.facebookET);
        TextView twitterET = root.findViewById(R.id.twitterET);

        Glide.with(requireContext()).load(mField.getImageUrl()).into(startupIcon);
        startupDescriptionET.setText(mField.getStartupDescription());
        startupFounderET.setText(mField.getStartupFounder());
        startupCoFounderET.setText(mField.getStartupCoFounder());
        startupTelephoneET.setText(mField.getTelephone());
        startupEmailET.setText(mField.getEmail());
        startupWebsiteET.setText(mField.getStartupWebsite());
        facebookET.setText(mField.getFacebookUrl());
        twitterET.setText(mField.getTwitterUrl());

        setupToolbar(startUpDetailsToolbar);
        return root;
    }

    private void setupToolbar(MaterialToolbar startUpDetailsToolbar) {
        startUpDetailsToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        startUpDetailsToolbar.setTitle(mField.getStartupName());
        startUpDetailsToolbar.inflateMenu(R.menu.edit_menu);
        startUpDetailsToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.edit_startup) {
                    editStartupDetails();
                }
                return true;
            }
        });
        startUpDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigateUp();
            }
        });
    }

    private void editStartupDetails() {
        // TODO: Edit startup details
    }
}
