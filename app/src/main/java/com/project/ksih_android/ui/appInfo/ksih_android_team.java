package com.project.ksih_android.ui.appInfo;


import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.MaterialToolbar;
import com.project.ksih_android.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ksih_android_team extends Fragment {

    MaterialToolbar mToolbar;


    public ksih_android_team() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_ksih_android_team, container, false);
        mToolbar = mView.findViewById(R.id.toolbar_about_developers);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        return mView;
    }

}
