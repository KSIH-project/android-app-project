package com.project.ksih_android.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentRegisterBinding;

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
                createNewUser(registrationFields.getEmail(), registrationFields.getConfirmPassword());
            }
        });
    }

    private void createNewUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success
                    Timber.d("CreateUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Check your email for verification mail", Toast.LENGTH_SHORT).show();
                                // TODO: Open email app option
                                navigateToLoginFragment(mRegisterBinding.buttonSignIn);
                            } else {
                                Toast.makeText(getContext(), "Couldn't send verification mail. Try again", Toast.LENGTH_SHORT).show();
                                Timber.d("Sending Verification Failed: " + task.getException().getMessage());
                            }
                        }
                    });
                } else {
                    // Sign in fails
                    Toast.makeText(getContext(), "Couldn't create new account", Toast.LENGTH_SHORT).show();
                    Timber.d("CreateUserWithEmail:failure \n" + task.getException().getMessage());
                }
            }
        });
    }

    private void navigateToLoginFragment(View v) {
        Navigation.findNavController(v).navigate(R.id.loginFragment);
    }
}
