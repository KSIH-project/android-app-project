package com.project.ksih_android.ui.zoom;


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
public class ZoomFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zoom, container, false);
        MaterialToolbar toolbar = view.findViewById(R.id.zoom_image_toolbar);
        navigateBack(toolbar);
        return view;
    }

    private void navigateBack(MaterialToolbar toolbar) {
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
    }
}
