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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ksih_android.R;
import com.project.ksih_android.data.StartUpField;

import static com.project.ksih_android.utility.Constants.EDIT_STARTUP_DETAILS_KEY;
import static com.project.ksih_android.utility.Constants.STARTUP_DETAILS_BUNDLE_KEY;
import static com.project.ksih_android.utility.Constants.STARTUP_FIREBASE_DATABASE_REFERENCE;

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

        mField = (StartUpField) getArguments().getSerializable(STARTUP_DETAILS_BUNDLE_KEY);
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
        startUpDetailsToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.edit_startup) {
                editStartupDetails();
            } else if (item.getItemId() == R.id.delete_startup) {
                deleteStartup(mField.getId());
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
                Toast.makeText(requireContext(), "Item removed!", Toast.LENGTH_SHORT).show();
                deleteImage(mField.getImageUrl());
                Navigation.findNavController(requireView()).navigate(R.id.navigation_startup);
            } else {
                Toast.makeText(requireContext(), "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteImage(String fullUrl) {
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(fullUrl);
        ref.delete().addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                Toast.makeText(requireContext(), "Image Removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Image Delete Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
