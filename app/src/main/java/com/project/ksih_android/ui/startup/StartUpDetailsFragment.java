package com.project.ksih_android.ui.startup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ksih_android.R;
import com.project.ksih_android.data.StartUpField;
import com.victor.loading.rotate.RotateLoading;

import static com.project.ksih_android.utility.Constants.EDIT_STARTUP_DETAILS_KEY;
import static com.project.ksih_android.utility.Constants.STARTUP_DETAILS_BUNDLE_KEY;
import static com.project.ksih_android.utility.Constants.STARTUP_FIREBASE_DATABASE_REFERENCE;
import static com.project.ksih_android.utility.Constants.ZOOM_IMAGE_GENERAL_KEY;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * {@link StartUpDetailsFragment} displays details about a startup
 */

public class StartUpDetailsFragment extends Fragment {

    private StartUpField mField;
    private RotateLoading progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_start_up_details, container, false);

        mField = (StartUpField) getArguments().getSerializable(STARTUP_DETAILS_BUNDLE_KEY);
        MaterialToolbar startUpDetailsToolbar = root.findViewById(R.id.startup_details_toolbar);
        ImageView startupLogo = root.findViewById(R.id.startup_detail_logo);
        TextView startupDescriptionET = root.findViewById(R.id.startupDescriptionET);
        TextView startupFounderET = root.findViewById(R.id.startupFounderET);
        TextView startupCoFounderET = root.findViewById(R.id.startupCoFounderET);
        TextView startupTelephoneET = root.findViewById(R.id.startupTelephoneET);
        TextView startupEmailET = root.findViewById(R.id.startupEmailET);
        TextView startupWebsiteET = root.findViewById(R.id.startupWebsiteET);
        TextView facebookET = root.findViewById(R.id.facebookET);
        TextView twitterET = root.findViewById(R.id.twitterET);
        progressBar = root.findViewById(R.id.startup_details_progress_bar);

        Glide.with(requireContext()).load(mField.getImageUrl()).into(startupLogo);
        startupDescriptionET.setText(mField.getStartupDescription());
        startupFounderET.setText(mField.getStartupFounder());
        startupCoFounderET.setText(mField.getStartupCoFounder());
        startupTelephoneET.setText(mField.getTelephone());
        startupEmailET.setText(mField.getEmail());
        startupWebsiteET.setText(mField.getStartupWebsite());
        facebookET.setText(mField.getFacebookUrl());
        twitterET.setText(mField.getTwitterUrl());

        // On Click of imageView, display a larger image that supports zooming
        initImageClick(startupLogo);

        setupToolbar(startUpDetailsToolbar);
        return root;
    }

    /**
     * A method that takes images to another fragment and enables image zoom
     * <p>
     * imageUrl is passed as a bundle parameter with the action that opens the net fragment
     *
     * @param startupLogo is the imageView that contains the image
     */
    private void initImageClick(ImageView startupLogo) {
        startupLogo.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(ZOOM_IMAGE_GENERAL_KEY, mField.getImageUrl());
            Navigation.findNavController(view).navigate(R.id.action_startUpDetailsFragment_to_zoomImageFragment, bundle);
        });
    }

    /**
     * A method that initializes the toolbar
     * <p>
     * It initializes menu item click listeners
     *
     * @param startUpDetailsToolbar represents the toolbar to be initialized
     */
    private void setupToolbar(MaterialToolbar startUpDetailsToolbar) {
        startUpDetailsToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        startUpDetailsToolbar.setTitle(mField.getStartupName());
        startUpDetailsToolbar.inflateMenu(R.menu.edit_menu);
        startUpDetailsToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.edit_startup) {
                editStartupDetails();
            } else if (item.getItemId() == R.id.delete_startup) {
                showDialog();
            }
            return true;
        });
        startUpDetailsToolbar.setNavigationOnClickListener(view -> Navigation.findNavController(view).navigateUp());
    }

    private void editStartupDetails() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EDIT_STARTUP_DETAILS_KEY, mField);
        Navigation.findNavController(requireView()).navigate(R.id.action_startUpDetailsFragment_to_addStartUpFragment, bundle);
    }

    private void deleteStartup(String id) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(STARTUP_FIREBASE_DATABASE_REFERENCE);
        ref.child(id).removeValue().addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                deleteImage(mField.getImageUrl());
            } else {
                Toast.makeText(requireContext(), "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                stopProgressBar(progressBar);
            }
        });
    }

    /**
     * Deletes an image from the firebase storage
     *
     * @param fullUrl is the url of the specific image that is to be deleted
     */
    private void deleteImage(String fullUrl) {
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(fullUrl);
        ref.delete().addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                Navigation.findNavController(requireView()).navigate(R.id.navigation_startup);
                stopProgressBar(progressBar);
                Toast.makeText(requireContext(), "Startup Removed", Toast.LENGTH_SHORT).show();
            } else {
                stopProgressBar(progressBar);
                Toast.makeText(requireContext(), "Image Delete Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Pops up a dialog to warn the user about deleting a startup entry
     */
    private void showDialog() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(requireContext());
        dialog.setMessage("Are you sure you want to delete this Startup?")
                .setPositiveButton("YES", (dialogInterface, i) -> {
                    startProgressBar(progressBar);
                    deleteStartup(mField.getId());
                    dialogInterface.dismiss();
                })
                .setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.dismiss());
        dialog.create().show();
    }


    private void startProgressBar(RotateLoading loading) {
        loading.start();
    }

    private void stopProgressBar(RotateLoading loading) {
        loading.stop();
    }
}
