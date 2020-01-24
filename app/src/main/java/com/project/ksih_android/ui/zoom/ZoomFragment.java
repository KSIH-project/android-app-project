package com.project.ksih_android.ui.zoom;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.project.ksih_android.R;

import static com.project.ksih_android.utility.Constants.ZOOM_IMAGE_GENERAL_KEY;

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
        ImageView zoomImageView = view.findViewById(R.id.image_zoom);
        if (getArguments() != null) {
            loadImage(zoomImageView, getArguments().getString(ZOOM_IMAGE_GENERAL_KEY));
        }
        navigateBack(toolbar);
        return view;
    }

    /**
     * Handles back press action when the back button is clicked
     *
     * @param toolbar is the toolbar that holds the back button
     */
    private void navigateBack(MaterialToolbar toolbar) {
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
    }

    /**
     * Loads image using {@link Glide} image loading library
     *
     * @param imageView is the view that the image is displayed in
     * @param imageUrl  is the url for the image that is passed from a bundle in the previous fragment
     */
    private void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(requireActivity()).load(imageUrl).into(imageView);
    }
}
