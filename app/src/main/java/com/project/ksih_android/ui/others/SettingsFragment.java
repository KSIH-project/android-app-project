package com.project.ksih_android.ui.others;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.ksih_android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

     TextView mTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        mTextView =root.findViewById(R.id.app_info);

        mTextView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_settings_to_appInfo));

        return root;
    }
}
