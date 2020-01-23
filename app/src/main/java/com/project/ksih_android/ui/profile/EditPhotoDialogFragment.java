package com.project.ksih_android.ui.profile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.ksih_android.R;
import com.project.ksih_android.data.User;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;
import static com.project.ksih_android.utility.Constants.PROFILE_FIREBASE_STORAGE_REFERENCE;
import static com.project.ksih_android.utility.Constants.PROFILE_IMAGE_REQUEST_CODE;
import static com.project.ksih_android.utility.Constants.ZOOM_IMAGE_GENERAL_KEY;

/**
 * A simple {@link Fragment} subclass.
 */

public class EditPhotoDialogFragment extends DialogFragment {

    private Bitmap mBitmap;
    private String imagePath = "";
    private String imageUrl;
    private DatabaseReference mRef;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_photo, null);
        builder.setView(view);
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openGallery();
            }
        });
        String photoUrl = null;
        if (getArguments() != null) {
            photoUrl = getArguments().getString("photo_url");
        }
        ImageView imageView = view.findViewById(R.id.profile_imageView);
        Glide.with(view.getContext())
                .load(photoUrl)
                .error(R.drawable.ic_profile_photo)
                .into(imageView);
        String finalPhotoUrl = photoUrl;
        view.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString(ZOOM_IMAGE_GENERAL_KEY, finalPhotoUrl);
            Navigation.findNavController(getParentFragment().getActivity(), R.id.nav_host_fragment).navigate(R.id.action_editPhotoFragment_to_zoomFragment, bundle);
        });
        return builder.create();
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PROFILE_IMAGE_REQUEST_CODE);
    }

    private void uploadPhoto() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
        byte[] data = outputStream.toByteArray();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference(PROFILE_FIREBASE_STORAGE_REFERENCE + imagePath);
        Timber.d("Image Path: %s", imagePath);
        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Upload successful
                // Get image download URL
                task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        imageUrl = task1.getResult().toString();
                        mRef.child("user_image").setValue(imageUrl).addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                Toast.makeText(getParentFragment().getContext(), "Image Saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getParentFragment().getContext(), "Unable to save image", Toast.LENGTH_SHORT).show();
                                Timber.d("Image Upload Error: %s", task2.getException().getLocalizedMessage());
                            }
                        });
                        Timber.d("Download URL: %s", imageUrl);
                    } else {
                        Timber.d("Download URL error: %s", task1.getException().getLocalizedMessage());
                        Toast.makeText(getParentFragment().getContext(), task1.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Timber.d("Image upload error: %s", task.getException().getLocalizedMessage());
                Toast.makeText(getParentFragment().getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == PROFILE_IMAGE_REQUEST_CODE) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    imagePath = imageUri.getLastPathSegment();
                    uploadPhoto();
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
}
