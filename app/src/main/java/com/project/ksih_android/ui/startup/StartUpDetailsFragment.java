package com.project.ksih_android.ui.startup;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.ksih_android.R;

/**
 * A simple {@link Fragment} subclass.
 *
 * It displays details about a startup
 */
public class StartUpDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_up_details, container, false);
    }

}
