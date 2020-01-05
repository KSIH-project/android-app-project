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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentAddStartUpBinding;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;
import static com.project.ksih_android.utility.Constants.REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * It contains input fields that allow for the addition of start ups
 */

public class AddStartUpFragment extends Fragment {

    private StartupViewModel mStartupViewModel;
    private Bitmap mBitmap;
    private String imagePath = "";
    private ImageView galleryIcon;

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
        mStartupViewModel = ViewModelProviders.of(this).get(StartupViewModel.class);
        setUpToolbar(addStartUpBinding);
        galleryIcon = addStartUpBinding.galleryIcon;
        galleryIcon.setOnClickListener(view -> openGallery());
        addStartUpBinding.saveStartup.setOnClickListener(view -> {
            if (mBitmap != null)
                uploadImageToFirebaseStorage();
            else
                Toast.makeText(requireActivity(), "Select an image", Toast.LENGTH_SHORT).show();
        });
        return addStartUpBinding.getRoot();
    }

    private void setUpToolbar(FragmentAddStartUpBinding addStartUpBinding) {
        MaterialToolbar addStartUpToolbar = addStartUpBinding.addStartupToolbar;
        addStartUpToolbar.setTitle("Add Startup");
        addStartUpToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        addStartUpToolbar.setNavigationOnClickListener(view -> Navigation.findNavController(view).navigateUp());
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
                        String imageUrl = task1.getResult().toString();
                        Timber.d("Download URL: %s", imageUrl);
                        Toast.makeText(getActivity(), imageUrl, Toast.LENGTH_SHORT).show();
                    } else {
                        Timber.d(task1.getException().getLocalizedMessage());
                    }
                });
            } else {
                Timber.d(task.getException().getLocalizedMessage());
                Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
