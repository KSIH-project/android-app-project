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
        String photoUrl = null;
            if (getArguments() != null)
            photoUrl = getArguments().getString("photo_url");

        ImageView imageView = view.findViewById(R.id.profile_imageView);
        Glide.with(view.getContext())
                .load(photoUrl)
                .error(R.drawable.ic_profile_photo)
                .into(imageView);

        String finalPhotoUrl = photoUrl;
        imageView.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString(ZOOM_IMAGE_GENERAL_KEY, getArguments().getString("photo_url"));
            Navigation.findNavController(getParentFragment().getActivity(), R.id.nav_host_fragment).navigate(R.id.action_editPhotoFragment_to_messageRecyclerView, bundle);
        });

        return builder.create();
    }
}
