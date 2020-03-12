package com.project.ksih_android.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentRegisterBinding;
import com.victor.loading.rotate.RotateLoading;

import timber.log.Timber;

/**
 * Created by SegunFrancis
 */

public class RegisterFragment extends Fragment {
    private FirebaseAuth mAuth;
    private RegistrationViewModel mViewModel;
    private FragmentRegisterBinding mRegisterBinding;
    private DatabaseReference storeDefaultDatabaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        return setUpBindings(savedInstanceState, inflater, container);
    }

    private View setUpBindings(Bundle savedInstanceState, LayoutInflater inflater, ViewGroup container) {
        mRegisterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        mViewModel = ViewModelProviders.of(this).get(RegistrationViewModel.class);
        if (savedInstanceState == null) {
            mViewModel.init();
        }
        mRegisterBinding.setRegisterModel(mViewModel);
        setUpButtonClick();
        return mRegisterBinding.getRoot();
    }

    private void setUpButtonClick() {
        mViewModel.getButtonClick().observe(this, registrationFields -> {
            startProgressBar(mRegisterBinding.progressBar);
            hideButton(mRegisterBinding.buttonRegister);
            createNewUser(registrationFields.getEmail(), registrationFields.getConfirmPassword());
        });
    }

    private void createNewUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                //send default data
                String deviceToken = FirebaseInstanceId.getInstance().getToken();

                //get and link storage
                String current_userID = mAuth.getCurrentUser().getUid();
                String name = email.substring(0, email.lastIndexOf("@"));

                storeDefaultDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(current_userID);

                storeDefaultDatabaseReference.child("user_name").setValue(name);
                storeDefaultDatabaseReference.child("user_firstName").setValue("");
                storeDefaultDatabaseReference.child("user_lastName").setValue("");
                storeDefaultDatabaseReference.child("user_email").setValue(email);
                storeDefaultDatabaseReference.child("user_mobile").setValue("");
                storeDefaultDatabaseReference.child("user_gender").setValue("");
                storeDefaultDatabaseReference.child("user_stack").setValue("");
                storeDefaultDatabaseReference.child("user_linkedInUrl").setValue("");
                storeDefaultDatabaseReference.child("user_githubUrl").setValue("");
                storeDefaultDatabaseReference.child("user_facebookUrl").setValue("");
                storeDefaultDatabaseReference.child("user_twitterUrl").setValue("");
                storeDefaultDatabaseReference.child("user_mediumUrl").setValue("");
                storeDefaultDatabaseReference.child("created_at").setValue(ServerValue.TIMESTAMP);
                storeDefaultDatabaseReference.child("user_image").setValue("default image");
                storeDefaultDatabaseReference.child("device_token").setValue(deviceToken);
                storeDefaultDatabaseReference.child("user_id").setValue(current_userID)
                        .addOnCompleteListener(task1 -> {
                            // Sign in success
                            Timber.d("CreateUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    Toast.makeText(getContext(), "Check your email for verification mail", Toast.LENGTH_SHORT).show();
                                    stopProgressBar(mRegisterBinding.progressBar);
                                    showButton(mRegisterBinding.buttonRegister);
                                    navigateToLoginFragment(mRegisterBinding.buttonRegister);
                                    logout();
                                } else {
                                    assert getParentFragment() != null;
                                    Toast.makeText(getParentFragment().getContext(), "Couldn't send verification mail. Try again", Toast.LENGTH_SHORT).show();
                                    logout();
                                    stopProgressBar(mRegisterBinding.progressBar);
                                    showButton(mRegisterBinding.buttonRegister);
                                    Timber.d("Sending Verification Failed: %s", task2.getException().getMessage());
                                }
                            });
                        });
                // Sign in success
                Timber.d("CreateUserWithEmail:success");
                FirebaseUser user = mAuth.getCurrentUser();
                user.sendEmailVerification().addOnCompleteListener(task12 -> {
                    if (task12.isSuccessful()) {
                        Toast.makeText(getContext(), "Check your email for verification mail", Toast.LENGTH_SHORT).show();
                        stopProgressBar(mRegisterBinding.progressBar);
                        showButton(mRegisterBinding.buttonRegister);
                        navigateToLoginFragment(mRegisterBinding.buttonRegister);
                    } else {
                        Toast.makeText(getContext(), "Couldn't send verification mail. " + task12.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        stopProgressBar(mRegisterBinding.progressBar);
                        showButton(mRegisterBinding.buttonRegister);
                        Timber.d("Sending Verification Failed: %s", task12.getException().getMessage());
                    }
                });
            } else {
                // Sign in fails
                Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                stopProgressBar(mRegisterBinding.progressBar);
                showButton(mRegisterBinding.buttonRegister);
                Timber.d("CreateUserWithEmail:failure %s", task.getException().getMessage());
            }
        });
    }

    private void logout() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
    }

    private void navigateToLoginFragment(View v) {
        Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment);
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
}
