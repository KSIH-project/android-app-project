package com.project.ksih_android.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentRegisterBinding;
import com.victor.loading.rotate.RotateLoading;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
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
        mRegisterBinding.signUpToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });
        mRegisterBinding.setRegisterModel(mViewModel);
        setUpButtonClick();
        return mRegisterBinding.getRoot();
    }

    private void  setUpButtonClick() {
        mViewModel.getButtonClick().observe(this, new Observer<RegistrationFields>() {
            @Override
            public void onChanged(RegistrationFields registrationFields) {
                startProgressBar(mRegisterBinding.progressBar);
                hideButton(mRegisterBinding.buttonRegister);
                createNewUser(registrationFields.getEmail(), registrationFields.getConfirmPassword());
            }
        });
    }

    private void createNewUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //send default data
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();

                    //get and link storage
                    String current_userID = mAuth.getCurrentUser().getUid();
                    String name = email.substring(0, email.lastIndexOf("@"));

                    storeDefaultDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(current_userID);

                    storeDefaultDatabaseReference.child("user_name").setValue(name);
                    storeDefaultDatabaseReference.child("verified").setValue("false");
                    storeDefaultDatabaseReference.child("search_name").setValue(name.toLowerCase());
                    storeDefaultDatabaseReference.child("user_mobile").setValue("");
                    storeDefaultDatabaseReference.child("user_email").setValue(email);
                    storeDefaultDatabaseReference.child("user_nickname").setValue("");
                    storeDefaultDatabaseReference.child("user_gender").setValue("");
                    storeDefaultDatabaseReference.child("user_profession").setValue("");
                    storeDefaultDatabaseReference.child("created_at").setValue(ServerValue.TIMESTAMP);
                    storeDefaultDatabaseReference.child("user_status").setValue("Hi, I'm new here");
                    storeDefaultDatabaseReference.child("user_image").setValue("default_image");
                    storeDefaultDatabaseReference.child("device_token").setValue(deviceToken);
                    storeDefaultDatabaseReference.child("user_thumb_image").setValue("default_image")
                            .addOnCompleteListener(task1 -> {
                                // Sign in success
                                Timber.d("CreateUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                user.sendEmailVerification().addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task1) {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(getContext(), "Check your email for verification mail", Toast.LENGTH_SHORT).show();
                                            stopProgressBar(mRegisterBinding.progressBar);
                                            showButton(mRegisterBinding.buttonRegister);
                                            // TODO: Open email app option
                                            navigateToLoginFragment(mRegisterBinding.buttonRegister);
                                        } else {
                                            Toast.makeText(getContext(), "Couldn't send verification mail. Try again", Toast.LENGTH_SHORT).show();
                                            stopProgressBar(mRegisterBinding.progressBar);
                                            showButton(mRegisterBinding.buttonRegister);
                                            Timber.d("Sending Verification Failed: %s", task1.getException().getMessage());
                                        }
                                    }
                                });
                            });


                } else {
                    // Sign in fails
                    Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    stopProgressBar(mRegisterBinding.progressBar);
                    showButton(mRegisterBinding.buttonRegister);
                    Timber.d("CreateUserWithEmail:failure \n" + task.getException().getMessage());
                }
            }
        });
    }

    private void navigateToLoginFragment(View v) {
        Navigation.findNavController(v).navigate(R.id.loginFragment);
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
