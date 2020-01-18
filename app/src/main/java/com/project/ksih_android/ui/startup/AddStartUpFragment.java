package com.project.ksih_android.ui.startup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import timber.log.Timber;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;
import com.project.ksih_android.data.StartUpField;
import com.victor.loading.rotate.RotateLoading;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;
import static com.project.ksih_android.utility.Constants.EDIT_STARTUP_DETAILS_KEY;
import static com.project.ksih_android.utility.Constants.STARTUP_FIREBASE_DATABASE_REFERENCE;
import static com.project.ksih_android.utility.Constants.STARTUP_FIREBASE_STORAGE_REFERENCE;
import static com.project.ksih_android.utility.Constants.REQUEST_CODE;
import static com.project.ksih_android.utility.Methods.hideSoftKeyboard;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * It contains input fields that allow for the addition of start ups
 */

public class AddStartUpFragment extends Fragment implements TextWatcher {

    private Bitmap mBitmap;
    private String imagePath = "";
    private StartUpField mField;
    private StartupValidationField mValidationField;

    private MaterialToolbar addStartUpToolbar;
    private MaterialButton saveStartupButton;
    private RotateLoading progressBar;
    private RoundedImageView galleryIcon;
    private TextInputEditText startupName;
    private TextInputEditText startupDescription;
    private TextInputEditText startupFounder;
    private TextInputEditText startupCoFounder;
    private TextInputEditText startupWebsite;
    private TextInputEditText facebookUrl;
    private TextInputEditText twitterUrl;
    private TextInputEditText telephone;
    private TextInputEditText email;

    private TextInputLayout startupNameLayout;
    private TextInputLayout descriptionLayout;
    private TextInputLayout founderLayout;
    private TextInputLayout websiteLayout;
    private TextInputLayout facebookLayout;
    private TextInputLayout twitterLayout;
    private TextInputLayout telephoneLayout;
    private TextInputLayout emailLayout;

    private String imageUrl;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StartupViewModel viewModel = ViewModelProviders.of(this).get(StartupViewModel.class);
        mBitmap = null;
        mValidationField = new StartupValidationField();
        return setUpBinding(inflater, container);
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
                Toast.makeText(getParentFragment().getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private View setUpBinding(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_add_start_up, container, false);
        setUpToolbar(view);
        saveStartupButton = view.findViewById(R.id.save_startup);
        progressBar = view.findViewById(R.id.startup_progress_bar);
        startupName = view.findViewById(R.id.startup_name);
        startupDescription = view.findViewById(R.id.description);
        startupFounder = view.findViewById(R.id.founder);
        startupCoFounder = view.findViewById(R.id.co_founder);
        startupWebsite = view.findViewById(R.id.website);
        facebookUrl = view.findViewById(R.id.facebook_url);
        twitterUrl = view.findViewById(R.id.twitter_url);
        telephone = view.findViewById(R.id.telephone);
        email = view.findViewById(R.id.email);
        galleryIcon = view.findViewById(R.id.gallery_icon);

        startupNameLayout = view.findViewById(R.id.layout_startup_name);
        descriptionLayout = view.findViewById(R.id.layout_description);
        founderLayout = view.findViewById(R.id.layout_founder);
        emailLayout = view.findViewById(R.id.layout_email);
        facebookLayout = view.findViewById(R.id.layout_facebook_url);
        telephoneLayout = view.findViewById(R.id.layout_telephone);
        twitterLayout = view.findViewById(R.id.layout_twitter_url);
        websiteLayout = view.findViewById(R.id.layout_website);

        startupName.addTextChangedListener(this);
        startupDescription.addTextChangedListener(this);
        startupFounder.addTextChangedListener(this);
        startupWebsite.addTextChangedListener(this);
        facebookUrl.addTextChangedListener(this);
        twitterUrl.addTextChangedListener(this);
        telephone.addTextChangedListener(this);
        email.addTextChangedListener(this);

        galleryIcon.setOnClickListener(v -> openGallery());
        saveStartupButton.setOnClickListener(v -> {
            // New Entry
            if (mField == null) {
                if (mBitmap != null && mValidationField.isButtonEnabled()) {
                    startProgressBar(progressBar);
                    hideButton(saveStartupButton);
                    uploadNewImageToFirebaseStorage();
                } else if (mBitmap == null) {
                    Toast.makeText(getParentFragment().getContext(), "Select an image", Toast.LENGTH_SHORT).show();
                } else if (!mValidationField.isButtonEnabled()){
                    Toast.makeText(getParentFragment().getContext(), "Fill required fields", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Update existing entry
                startProgressBar(progressBar);
                hideButton(saveStartupButton);
                uploadImageToFirebaseStorage();
            }
        });
        if (getArguments() != null) {
            mField = (StartUpField) getArguments().getSerializable(EDIT_STARTUP_DETAILS_KEY);
            getStartUpDetails();
            Timber.d("mField: %s", mField.getId());
        }
        return view;
    }

    private void setUpToolbar(View view) {
        addStartUpToolbar = view.findViewById(R.id.add_startup_toolbar);
        addStartUpToolbar.setTitle("Add Startup");
        addStartUpToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        addStartUpToolbar.setNavigationOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
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
        StorageReference storageReference = firebaseStorage.getReference(STARTUP_FIREBASE_STORAGE_REFERENCE + imagePath);
        Timber.d("Image Path: %s", imagePath);
        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Upload successful
                // Get image download URL
                task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        imageUrl = task1.getResult().toString();
                        addStartUp();
                        Timber.d("Download URL: %s", imageUrl);
                    } else {
                        Timber.d("Download URL error: %s", task1.getException().getLocalizedMessage());
                        Toast.makeText(getParentFragment().getContext(), task1.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        stopProgressBar(progressBar);
                        showButton(saveStartupButton);
                    }
                });
            } else {
                Timber.d("Image upload error: %s", task.getException().getLocalizedMessage());
                Toast.makeText(getParentFragment().getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                stopProgressBar(progressBar);
                showButton(saveStartupButton);
            }
        });
    }

    private void uploadImageToFirebaseStorage() {
        if (mBitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            byte[] data = outputStream.toByteArray();
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference(STARTUP_FIREBASE_STORAGE_REFERENCE + imagePath);
            Timber.d("Image Path: %s", imagePath);
            UploadTask uploadTask = storageReference.putBytes(data);
            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Upload successful
                    // Get image download URL
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            imageUrl = task1.getResult().toString();
                            editStartupDetails();
                            Timber.d("Download URL: %s", imageUrl);
                        } else {
                            Timber.d("Download URL error: %s", task1.getException().getLocalizedMessage());
                            stopProgressBar(progressBar);
                        }
                    });
                } else {
                    Timber.d(task.getException().getLocalizedMessage());
                    Toast.makeText(getParentFragment().getContext(), "Image upload error: %s" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    stopProgressBar(progressBar);
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
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference(STARTUP_FIREBASE_DATABASE_REFERENCE);
        String id = firebaseDatabase.push().getKey();
        StartUpField startUpField = new StartUpField(id, startupName.getText().toString(),
                startupDescription.getText().toString(), startupFounder.getText().toString(),
                startupCoFounder.getText().toString(), startupWebsite.getText().toString(),
                facebookUrl.getText().toString(), twitterUrl.getText().toString(), imageUrl,
                telephone.getText().toString(), email.getText().toString());
        firebaseDatabase.child(id).setValue(startUpField).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (getView() != null) {
                    Navigation.findNavController(requireView()).navigate(R.id.navigation_startup);
                }
                showButton(saveStartupButton);
                stopProgressBar(progressBar);
                Toast.makeText(getParentFragment().getContext(), "Startup added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getParentFragment().getContext(), "Unable to add Startup", Toast.LENGTH_SHORT).show();
                Timber.d("database Write Error: %s", task.getException().getLocalizedMessage());
                showButton(saveStartupButton);
                stopProgressBar(progressBar);
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
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference(STARTUP_FIREBASE_DATABASE_REFERENCE);
        String id = mField.getId();
        saveStartupButton.setEnabled(true);
        if (imageUrl == null) {
            // Admin did not change the original photo
            StartUpField startUpField = new StartUpField(id, startupName.getText().toString(),
                    startupDescription.getText().toString(), startupFounder.getText().toString(),
                    startupCoFounder.getText().toString(), startupWebsite.getText().toString(),
                    facebookUrl.getText().toString(), twitterUrl.getText().toString(), mField.getImageUrl(),
                    telephone.getText().toString(), email.getText().toString());
            firebaseDatabase.child(id).setValue(startUpField).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    stopProgressBar(progressBar);
                    if (getView() != null) {
                        Navigation.findNavController(requireView()).navigate(R.id.navigation_startup);
                    }
                    showButton(saveStartupButton);
                    Toast.makeText(getParentFragment().getContext(), "Startup edited", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getParentFragment().getContext(), "Unable to edit Startup", Toast.LENGTH_SHORT).show();
                    Timber.d("database Write Error: %s", task.getException().getLocalizedMessage());
                    stopProgressBar(progressBar);
                    showButton(saveStartupButton);
                }
            });
        } else {
            // Admin changes the original photo
            StartUpField startUpField = new StartUpField(id, startupName.getText().toString(),
                    startupDescription.getText().toString(), startupFounder.getText().toString(),
                    startupCoFounder.getText().toString(), startupWebsite.getText().toString(),
                    facebookUrl.getText().toString(), twitterUrl.getText().toString(), imageUrl,
                    telephone.getText().toString(), email.getText().toString());
            firebaseDatabase.child(id).setValue(startUpField).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    stopProgressBar(progressBar);
                    if (getView() != null) {
                        Navigation.findNavController(requireView()).navigate(R.id.navigation_startup);
                    }
                    showButton(saveStartupButton);
                    Toast.makeText(getParentFragment().getContext(), "Startup edited", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getParentFragment().getContext(), "Unable to edit Startup", Toast.LENGTH_SHORT).show();
                    Timber.d("Database Write Error: %s", task.getException().getLocalizedMessage());
                    stopProgressBar(progressBar);
                    showButton(saveStartupButton);
                }
            });
        }
    }

    private void startProgressBar(RotateLoading loading) {
        loading.start();
    }

    private void stopProgressBar(RotateLoading loading) {
        loading.stop();
    }

    private void showButton(MaterialButton button) {
        button.setVisibility(View.VISIBLE);
    }

    private void hideButton(MaterialButton button) {
        button.setVisibility(View.INVISIBLE);
    }

    /**
     * Implementation of the {@link TextWatcher} interface
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (startupNameLayout.hasFocus()) {
            mValidationField.validateStartupName(startupNameLayout, charSequence);
        } else if (founderLayout.hasFocus()) {
            mValidationField.validateFounderName(founderLayout, charSequence);
        } else if (descriptionLayout.hasFocus()) {
            mValidationField.validateDescription(descriptionLayout, charSequence);
        } else if (emailLayout.hasFocus()) {
            mValidationField.validateEmail(emailLayout, charSequence);
        } else if (telephoneLayout.hasFocus()) {
            mValidationField.validateTelephone(telephoneLayout, charSequence);
        } else if (websiteLayout.hasFocus()) {
            mValidationField.validateWebsite(websiteLayout, charSequence);
        } else if (facebookLayout.hasFocus()) {
            mValidationField.validateFacebookUrl(facebookLayout, charSequence);
        } else if (twitterLayout.hasFocus()) {
            mValidationField.validateTwitterUrl(twitterLayout, charSequence);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        saveStartupButton.setEnabled(mValidationField.isButtonEnabled());
    }
}
