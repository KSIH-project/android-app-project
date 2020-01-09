package com.project.ksih_android.ui.startup;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import timber.log.Timber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.ksih_android.R;
import com.project.ksih_android.storage.SharedPreferencesStorage;

import static com.project.ksih_android.utility.Constants.STARTUP_ITEM_KEY;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * It displays details about a startup
 */
public class StartUpDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.content_fragment_startup_details, container, false);
        SharedPreferencesStorage mStorage = new SharedPreferencesStorage(requireContext());
        StartUpField field = mStorage.getStartUpField(STARTUP_ITEM_KEY);

        TextView startupDescriptionET = root.findViewById(R.id.startupDescriptionET);
        TextView startupFounderET = root.findViewById(R.id.startupFounderET);
        TextView startupCoFounderET = root.findViewById(R.id.startupCoFounderET);
        TextView startupTelephoneET = root.findViewById(R.id.startupTelephoneET);
        TextView startupEmailET = root.findViewById(R.id.startupEmailET);
        TextView startupWebsiteET = root.findViewById(R.id.startupWebsiteET);
        TextView facebookET = root.findViewById(R.id.facebookET);
        TextView twitterET = root.findViewById(R.id.twitterET);

//        Glide.with(requireContext()).load(field.getImageUrl()).into(startupIcon);
        startupDescriptionET.setText(field.getStartupDescription());
        startupFounderET.setText(field.getStartupFounder());
        startupCoFounderET.setText(field.getStartupCoFounder());
        startupTelephoneET.setText(field.getTelephone());
        startupEmailET.setText(field.getEmail());
        startupWebsiteET.setText(field.getStartupWebsite());
        facebookET.setText(field.getFacebookUrl());
        twitterET.setText(field.getTwitterUrl());
        return root;
    }
}
