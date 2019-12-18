package com.project.ksih_android.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentLoginBinding;
import com.project.ksih_android.ui.HomeActivity;
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

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private FirebaseAuth mAuth;
    private FragmentLoginBinding mLoginBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        return setUpBindings(savedInstanceState, inflater, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView registerText = view.findViewById(R.id.register_text);
        TextView forgotPasswordText = view.findViewById(R.id.forgot_password_text);

        navigateToRegisterFragment(registerText);
        navigateToForgotPasswordFragment(forgotPasswordText);
    }

    private View setUpBindings(Bundle savedInstanceState,LayoutInflater inflater, ViewGroup container) {
        mLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        if (savedInstanceState == null) {
            mViewModel.init();
        }
        mLoginBinding.setLogin(mViewModel);
        setUpButtonClick();
        return mLoginBinding.getRoot();
    }

    private void setUpButtonClick() {
        mViewModel.getButtonClick().observe(this, new Observer<LoginFields>() {
            @Override
            public void onChanged(LoginFields loginFields) {
                startProgressBar(mLoginBinding.progressBar);
                hideButton(mLoginBinding.buttonSignIn);
                signInUser(loginFields.getEmail(), loginFields.getPassword());
            }
        });
    }

    private void signInUser(final String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Timber.d("Log in successful");
                    if (!isUserVerified(mAuth.getCurrentUser())) {
                        Toast.makeText(getActivity(), "Your Email has not been verified", Toast.LENGTH_SHORT).show();
                        stopProgressBar(mLoginBinding.progressBar);
                        showButton(mLoginBinding.buttonSignIn);
                        logout();
                        Timber.d("UserNotVerified");
                    } else {
                        Timber.d("UserIsVerified");
                        stopProgressBar(mLoginBinding.progressBar);
                        showButton(mLoginBinding.buttonSignIn);
                        Toast.makeText(getActivity(), "Sing in successful", Toast.LENGTH_SHORT).show();
                        navigateToHomeActivity();
                    }
                } else {
                    stopProgressBar(mLoginBinding.progressBar);
                    showButton(mLoginBinding.buttonSignIn);
                    Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Timber.d("SignInError: " + task.getException().getMessage());
                }
            }
        });
    }

    private boolean isUserVerified(FirebaseUser user) {
        return user.isEmailVerified();
    }

    private void logout() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
    }

    private void navigateToHomeActivity() {
        startActivity(new Intent(requireActivity(), HomeActivity.class));
    }

    private void navigateToRegisterFragment(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });
    }

    private void navigateToForgotPasswordFragment(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
            }
        });
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
