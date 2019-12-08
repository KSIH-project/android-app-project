package com.project.ksih_android.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentLoginBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

/**
 * Created by SegunFrancis
 */
public class LoginFragment extends Fragment {

    private AuthViewModel mViewModel;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setUpBindings(savedInstanceState, inflater, container);
    }

    private View setUpBindings(Bundle savedInstanceState, LayoutInflater inflater, ViewGroup container) {
        FragmentLoginBinding loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        navigateToForgottenPasswordFragment(loginBinding.forgotPasswordText);
        navigateToRegisterFragment(loginBinding.registerText);
        mViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        if (savedInstanceState == null) {
            mViewModel.init();
        }
        loginBinding.setLogin(mViewModel);
        setUpButtonClick();
        return loginBinding.getRoot();
    }

    private void setUpButtonClick() {
        mViewModel.getButtonClick().observe(this, new Observer<AuthFields>() {
            @Override
            public void onChanged(AuthFields authFields) {
                //TODO: Navigate to Home Activity
            }
        });
    }

    private void navigateToForgottenPasswordFragment(TextView view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
            }
        });
    }

    private void navigateToRegisterFragment(TextView view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });
    }
}
