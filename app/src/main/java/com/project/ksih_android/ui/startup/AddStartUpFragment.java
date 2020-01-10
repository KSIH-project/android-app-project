package com.project.ksih_android.ui.startup;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import timber.log.Timber;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentAddStartUpBinding;
import com.project.ksih_android.storage.SharedPreferencesStorage;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;
import static com.project.ksih_android.utility.Constants.EDIT_STARTUP_ITEM_KEY;
import static com.project.ksih_android.utility.Constants.REQUEST_CODE;
import static com.project.ksih_android.utility.Methods.hideSoftKeyboard;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * It contains input fields that allow for the addition of start ups
 */

public class AddStartUpFragment extends Fragment {

    private StartupViewModel mStartupViewModel;
    private Bitmap mBitmap;
    private String imagePath = "";

    private MaterialToolbar addStartUpToolbar;
    private ImageView galleryIcon;
    private TextInputEditText startupName;
    private TextInputEditText startupDescription;
    private TextInputEditText startupFounder;
    private TextInputEditText startupCoFounder;
    private TextInputEditText startupWebsite;
    private TextInputEditText facebookUrl;
    private TextInputEditText twitterUrl;
    private TextInputEditText telephone;
    private TextInputEditText email;
    private String imageUrl;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mStartupViewModel = ViewModelProviders.of(this).get(StartupViewModel.class);
        mBitmap = null;
        mStartupViewModel.getStartupState().observe(this, new Observer<StartupViewModel.STATE>() {
            @Override
            public void onChanged(StartupViewModel.STATE state) {

            }
        });
        return setUpBinding(savedInstanceState, inflater, container);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    imagePath = imageUri.getLastPathSegment();
                    if (Build.VERSION.SDK_INT >= 29) {
                        ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(), imageUri);
                        mBitmap = ImageDecoder.decodeBitmap(source);
                        // Load image using Glide
                        Glide.with(requireContext()).load(mBitmap).into(galleryIcon);
                    } else {
                        mBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                        // Load image using Glide
                        Glide.with(requireContext()).load(mBitmap).into(galleryIcon);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Timber.d(e.getLocalizedMessage());
                }
            } else {
                Toast.makeText(requireActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private View setUpBinding(Bundle savedinstanceState, LayoutInflater inflater, ViewGroup container) {
        FragmentAddStartUpBinding addStartUpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_start_up, container, false);
        mStartupViewModel = ViewModelProviders.of(this).get(StartupViewModel.class);
        addStartUpToolbar = addStartUpBinding.addStartupToolbar;
        setUpToolbar();
        startupName = addStartUpBinding.startupName;
        startupDescription = addStartUpBinding.description;
        startupFounder = addStartUpBinding.founder;
        startupCoFounder = addStartUpBinding.coFounder;
        startupWebsite = addStartUpBinding.website;
        facebookUrl = addStartUpBinding.facebookUrl;
        twitterUrl = addStartUpBinding.twitterUrl;
        telephone = addStartUpBinding.telephone;
        email = addStartUpBinding.email;
        galleryIcon = addStartUpBinding.galleryIcon;
        galleryIcon.setOnClickListener(view -> openGallery());
        editStartUpDetails();
        addStartUpBinding.saveStartup.setOnClickListener(view -> {
            if (mBitmap != null)
                uploadImageToFirebaseStorage();
            else
                Toast.makeText(requireActivity(), "Select an image", Toast.LENGTH_SHORT).show();
        });
        return addStartUpBinding.getRoot();
    }

    private void setUpToolbar() {
        addStartUpToolbar.setTitle("Add Startup");
        addStartUpToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        addStartUpToolbar.setNavigationOnClickListener(view -> {
            Navigation.findNavController(view).navigateUp();
            hideSoftKeyboard(requireActivity());
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void uploadImageToFirebaseStorage() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
        byte[] data = outputStream.toByteArray();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference("images/startup_logo/" + imagePath);
        Timber.d("Image Path: %s", imagePath);
        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Upload successful
                // Get image download URL
                task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(requireActivity(), task1 -> {
                    if (task1.isSuccessful()) {
                        imageUrl = task1.getResult().toString();
                        addStartUp();
                        Timber.d("Download URL: %s", imageUrl);
                        Toast.makeText(getActivity(), imageUrl, Toast.LENGTH_SHORT).show();
                    } else {
                        Timber.d("Download URL error: %s", task1.getException().getLocalizedMessage());
                    }
                });
            } else {
                Timber.d("Image upload error: %s", task.getException().getLocalizedMessage());
                Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Creates new startup object and adds it to firebase database
     */
    private void addStartUp() {
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("startups");
        String id = firebaseDatabase.push().getKey();
        StartUpField startUpField = new StartUpField(id, startupName.getText().toString(),
                startupDescription.getText().toString(), startupFounder.getText().toString(),
                startupCoFounder.getText().toString(), startupWebsite.getText().toString(),
                facebookUrl.getText().toString(), twitterUrl.getText().toString(), imageUrl,
                telephone.getText().toString(), email.getText().toString());
        firebaseDatabase.child(id).setValue(startUpField);
    }

    private void editStartUpDetails() {
        SharedPreferencesStorage pref = new SharedPreferencesStorage(requireContext());
        StartUpField field = pref.getStartUpField(EDIT_STARTUP_ITEM_KEY);
        if (field != null) {
            Glide.with(requireContext()).load(field.getImageUrl()).into(galleryIcon);
            addStartUpToolbar.setTitle(field.getStartupName());
            startupName.setText(field.getStartupName());
            startupDescription.setText(field.getStartupDescription());
            startupFounder.setText(field.getStartupFounder());
            startupCoFounder.setText(field.getStartupCoFounder());
            startupWebsite.setText(field.getStartupWebsite());
            facebookUrl.setText(field.getFacebookUrl());
            twitterUrl.setText(field.getTwitterUrl());
            telephone.setText(field.getTelephone());
            email.setText(field.getEmail());

            DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("startups");
            String id = field.getId();
            if (imageUrl == null) {
                // Admin did not change the original photo
                imageUrl = field.getImageUrl();
                StartUpField startUpField = new StartUpField(id, startupName.getText().toString(),
                        startupDescription.getText().toString(), startupFounder.getText().toString(),
                        startupCoFounder.getText().toString(), startupWebsite.getText().toString(),
                        facebookUrl.getText().toString(), twitterUrl.getText().toString(), imageUrl,
                        telephone.getText().toString(), email.getText().toString());
                firebaseDatabase.child(id).setValue(startUpField);
            } else {
                // Admin changes the original photo
                StartUpField startUpField = new StartUpField(id, startupName.getText().toString(),
                        startupDescription.getText().toString(), startupFounder.getText().toString(),
                        startupCoFounder.getText().toString(), startupWebsite.getText().toString(),
                        facebookUrl.getText().toString(), twitterUrl.getText().toString(), imageUrl,
                        telephone.getText().toString(), email.getText().toString());
                firebaseDatabase.child(id).setValue(startUpField);
            }
        }
    }
}
