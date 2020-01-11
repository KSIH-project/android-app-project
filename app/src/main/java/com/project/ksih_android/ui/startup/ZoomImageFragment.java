package com.project.ksih_android.ui.startup;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentZoomImageBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class ZoomImageFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentZoomImageBinding root = DataBindingUtil.inflate(inflater, R.layout.fragment_zoom_image, container, false);
        root.zoomImageToolbar.setNavigationOnClickListener(view -> Navigation.findNavController(view).navigateUp());
        if (getArguments() != null) {
            Glide.with(requireContext()).load(getArguments().getString("scale_image")).into(root.startupImageLarge);
        }
        return root.getRoot();
    }
}
