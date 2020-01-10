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
import com.project.ksih_android.data.StartUpField;
import com.project.ksih_android.databinding.FragmentAddStartUpBinding;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;
import static com.project.ksih_android.utility.Constants.REQUEST_CODE;
import static com.project.ksih_android.utility.Methods.hideSoftKeyboard;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * It contains input fields that allow for the addition of start ups
 */

public class AddStartUpFragment extends Fragment {

    private Bitmap mBitmap;
    private String imagePath = "";
    private StartUpField mField;

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
        mBitmap = null;
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
        addStartUpBinding.saveStartup.setOnClickListener(view -> {
            // New Entry
            if (mField == null) {
                if (mBitmap != null)
                    uploadNewImageToFirebaseStorage();
                else
                    Toast.makeText(requireActivity(), "Select an image", Toast.LENGTH_SHORT).show();
            } else {
                // Update existing entry
                uploadImageToFirebaseStorage();
            }
        });
        if (getArguments() != null) {
            mField = (StartUpField) getArguments().getSerializable("edit_startup_details");
            getStartUpDetails();
            Timber.d("mField: %s", mField.getId());
        }
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

    private void uploadNewImageToFirebaseStorage() {
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

    private void uploadImageToFirebaseStorage() {
        if (mBitmap != null) {
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
                            editStartupDetails();
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
        } else {
            editStartupDetails();
        }
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
        firebaseDatabase.child(id).setValue(startUpField).addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                Navigation.findNavController(requireView()).navigate(R.id.navigation_startup);
            } else {
                Toast.makeText(requireContext(), "Unable to add Startup", Toast.LENGTH_SHORT).show();
                Timber.d("database Write Error: %s", task.getException().getLocalizedMessage());
            }
        });
    }

    private void getStartUpDetails() {
        Glide.with(requireContext()).load(mField.getImageUrl()).into(galleryIcon);
        addStartUpToolbar.setTitle(mField.getStartupName());
        startupName.setText(mField.getStartupName());
        startupDescription.setText(mField.getStartupDescription());
        startupFounder.setText(mField.getStartupFounder());
        startupCoFounder.setText(mField.getStartupCoFounder());
        startupWebsite.setText(mField.getStartupWebsite());
        facebookUrl.setText(mField.getFacebookUrl());
        twitterUrl.setText(mField.getTwitterUrl());
        telephone.setText(mField.getTelephone());
        email.setText(mField.getEmail());
    }

    private void editStartupDetails() {
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("startups");
        String id = mField.getId();
        if (imageUrl == null) {
            // Admin did not change the original photo
            StartUpField startUpField = new StartUpField(id, startupName.getText().toString(),
                    startupDescription.getText().toString(), startupFounder.getText().toString(),
                    startupCoFounder.getText().toString(), startupWebsite.getText().toString(),
                    facebookUrl.getText().toString(), twitterUrl.getText().toString(), mField.getImageUrl(),
                    telephone.getText().toString(), email.getText().toString());
            firebaseDatabase.child(id).setValue(startUpField).addOnCompleteListener(requireActivity(), task -> {
                if (task.isSuccessful()) {
                    Navigation.findNavController(requireView()).navigate(R.id.navigation_startup);
                } else {
                    Toast.makeText(requireContext(), "Unable to edit Startup", Toast.LENGTH_SHORT).show();
                    Timber.d("database Write Error: %s", task.getException().getLocalizedMessage());
                }
            });
        } else {
            // Admin changes the original photo
            StartUpField startUpField = new StartUpField(id, startupName.getText().toString(),
                    startupDescription.getText().toString(), startupFounder.getText().toString(),
                    startupCoFounder.getText().toString(), startupWebsite.getText().toString(),
                    facebookUrl.getText().toString(), twitterUrl.getText().toString(), imageUrl,
                    telephone.getText().toString(), email.getText().toString());
            firebaseDatabase.child(id).setValue(startUpField).addOnCompleteListener(requireActivity(), task -> {
                if (task.isSuccessful()) {
                    Navigation.findNavController(requireView()).navigate(R.id.navigation_startup);
                } else {
                    Toast.makeText(requireContext(), "Unable to edit Startup", Toast.LENGTH_SHORT).show();
                    Timber.d("database Write Error: %s", task.getException().getLocalizedMessage());
                }
            });
        }
    }
}
