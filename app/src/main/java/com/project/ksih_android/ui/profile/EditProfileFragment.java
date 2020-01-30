package com.project.ksih_android.ui.profile;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import timber.log.Timber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ksih_android.R;
import com.project.ksih_android.data.User;
import com.project.ksih_android.databinding.FragmentEditProfileBinding;
import com.victor.loading.rotate.RotateLoading;

import org.jetbrains.annotations.NotNull;

import static com.project.ksih_android.utility.Constants.PROFILE_FIREBASE_DATABASE_REFERENCE;

/**
 * A simple {@link Fragment} subclass.
 */

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding mProfileBinding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);
        EditProfileViewModel viewModel = ViewModelProviders.of(getActivity()).get(EditProfileViewModel.class);
        viewModel.init();
        User user = null;
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user_data");
            viewModel.preChangedData.setValue(user);
        }
        mProfileBinding.setEditUser(user);
        mProfileBinding.saveProfileButton.setOnClickListener(view ->  updateUserProfile());
        viewModel.postChangedData.setValue(mProfileBinding.getEditUser());
        return mProfileBinding.getRoot();
    }

    private void updateUserProfile() {
        if (mProfileBinding.firstNameEditText.getText().toString().isEmpty()) {
            Toast.makeText(getParentFragment().getContext(), "First name is required", Toast.LENGTH_SHORT).show();
            mProfileBinding.firstNameEditText.requestFocus();
        } else if (mProfileBinding.lastNameEditText.getText().toString().isEmpty()) {
            Toast.makeText(getParentFragment().getContext(), "Last name is required", Toast.LENGTH_SHORT).show();
            mProfileBinding.lastNameEditText.requestFocus();
        } else {
            startProgressBar(mProfileBinding.editProfileProgressBar);
            hideButton(mProfileBinding.saveProfileButton);
            String uid = FirebaseAuth.getInstance().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(PROFILE_FIREBASE_DATABASE_REFERENCE);
            ref.child(uid).setValue(mProfileBinding.getEditUser()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getParentFragment().getContext(), "Profile edit successful", Toast.LENGTH_SHORT).show();
                    stopProgressBar(mProfileBinding.editProfileProgressBar);
                    showButton(mProfileBinding.saveProfileButton);
                    navigateToProfileFragment(getParentFragment().getView());
                } else {
                    Toast.makeText(getParentFragment().getContext(), "Unable to edit profile", Toast.LENGTH_SHORT).show();
                    Timber.d("Edit profile error %s", task.getException().getLocalizedMessage());
                    stopProgressBar(mProfileBinding.editProfileProgressBar);
                    showButton(mProfileBinding.saveProfileButton);
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

    private void navigateToProfileFragment(View view) {
        Navigation.findNavController(view).navigate(R.id.action_editProfileFragment_to_profileFragment);
    }
}
