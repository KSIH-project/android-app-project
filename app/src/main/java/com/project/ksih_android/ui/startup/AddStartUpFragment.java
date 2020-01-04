package com.project.ksih_android.ui.startup;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import timber.log.Timber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;
    private Bitmap mBitmap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = firebaseStorage.getReference("images/");
        mBitmap = null;
        return setUpBinding(savedInstanceState, inflater, container);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(), imageUri);
                    mBitmap = ImageDecoder.decodeBitmap(source);
                    uploadImageToFirebaseStorage();
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
        mAuth = FirebaseAuth.getInstance();
        FragmentAddStartUpBinding addStartUpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_start_up, container, false);
        mStartupViewModel = ViewModelProviders.of(this).get(StartupViewModel.class);
        addStartUpBinding.galleryIcon.setOnClickListener(view -> openGallery());
        return addStartUpBinding.getRoot();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void uploadImageToFirebaseStorage() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] data = outputStream.toByteArray();
        UploadTask uploadTask = mStorageReference.putBytes(data);
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                task.addOnSuccessListener(taskSnapshot -> {
                    if (task.isSuccessful()) {
                        String imageUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                        Timber.d("Download URL: %s", imageUrl);
                        Toast.makeText(getActivity(), imageUrl, Toast.LENGTH_SHORT).show();
                    } else {
                        Timber.d(taskSnapshot.getError().getLocalizedMessage());
                    }
                });
            } else {
                Timber.d(task.getException().getLocalizedMessage());
                Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
