package com.project.ksih_android.ui.profile;


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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import timber.log.Timber;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.ksih_android.R;
import com.project.ksih_android.data.User;
import com.project.ksih_android.databinding.FragmentProfileBinding;
import com.project.ksih_android.storage.SharedPreferencesStorage;
import com.project.ksih_android.ui.sharedViewModel.SharedViewModel;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;
import static com.project.ksih_android.utility.Constants.EMAIL_KEY;
import static com.project.ksih_android.utility.Constants.PROFILE_FIREBASE_DATABASE_REFERENCE;
import static com.project.ksih_android.utility.Constants.PROFILE_FIREBASE_STORAGE_REFERENCE;
import static com.project.ksih_android.utility.Constants.PROFILE_IMAGE_REQUEST_CODE;
import static com.project.ksih_android.utility.Constants.PROFILE_PHOTO_KEY;
import static com.project.ksih_android.utility.Constants.USERNAME_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private User mUser;
    private Bitmap mBitmap;
    private String imagePath = "";
    private String imageUrl;
    private FragmentProfileBinding mProfileBinding;
    private DatabaseReference mRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String uid = FirebaseAuth.getInstance().getUid();
        mRef = FirebaseDatabase.getInstance().getReference(PROFILE_FIREBASE_DATABASE_REFERENCE).child(uid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,
                container, false);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                mProfileBinding.setUser(mUser);
                // Load user profile Image
                Glide.with(requireContext())
                        .load(mUser.user_image)
                        .placeholder(R.drawable.ic_profile_photo)
                        .error(R.drawable.ic_profile_photo)
                        .into(mProfileBinding.userProfileImage);
                // Add user details to shared preference
                SharedViewModel viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
                SharedPreferencesStorage pref = new SharedPreferencesStorage(getParentFragment().getContext());
                String username;
                if (mUser.user_firstName.length() != 0) {
                    username = mUser.user_firstName + " " + mUser.user_lastName;
                } else {
                    username = mUser.user_name;
                }
                pref.setUserName(USERNAME_KEY, username);
                pref.setUserEmail(EMAIL_KEY, mUser.user_email);
                pref.setProfilePhotoUrl(PROFILE_PHOTO_KEY, mUser.user_image);

                // Add data to viewModel in order to get an immediate update of any change
                viewModel.username.setValue(username);
                viewModel.userEmail.setValue(mUser.user_email);
                viewModel.userProfilePhotoUrl.setValue(mUser.user_image);

                Bundle bundle = new Bundle();
                bundle.putSerializable("user_data", mUser);
                mProfileBinding.editProfileButton.setOnClickListener(view -> Navigation.findNavController(view)
                        .navigate(R.id.action_profileFragment_to_editProfileFragment, bundle));


                mProfileBinding.userProfileImage.setOnClickListener(view -> {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("photo_url", mUser.user_image);
                    Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_editPhotoFragment, bundle1);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return mProfileBinding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == PROFILE_IMAGE_REQUEST_CODE) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    imagePath = imageUri.getLastPathSegment();
                    if (Build.VERSION.SDK_INT >= 29) {
                        ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(), imageUri);
                        mBitmap = ImageDecoder.decodeBitmap(source);
                        // Load image using Glide
                        Glide.with(requireContext())
                                .load(mBitmap)
                                .placeholder(R.drawable.ic_profile_photo)
                                .error(R.drawable.ic_profile_photo)
                                .into(mProfileBinding.userProfileImage);
                    } else {
                        mBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                        // Load image using Glide
                        Glide.with(requireContext())
                                .load(mBitmap)
                                .placeholder(R.drawable.ic_profile_photo)
                                .error(R.drawable.ic_profile_photo)
                                .into(mProfileBinding.userProfileImage);
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
}
