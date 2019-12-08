package com.project.ksih_android.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentLoginBinding;

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
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });
    }

    private View setUpBindings(Bundle savedInstanceState,LayoutInflater inflater, ViewGroup container) {
        FragmentLoginBinding loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        if (savedInstanceState == null) {
            mViewModel.init();
        }
        loginBinding.setLogin(mViewModel);
        setUpButtonClick();
        return loginBinding.getRoot();
    }

    private void setUpButtonClick() {
        mViewModel.getButtonClick().observe(this, new Observer<LoginFields>() {
            @Override
            public void onChanged(LoginFields loginFields) {
                //TODO: Navigate to Home Activity
                signInUser(loginFields.getEmail(), loginFields.getPassword());
            }
        });
    }

    private void signInUser(final String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Timber.d("Log in successful");
                    if (!isUserVerified(mAuth.getCurrentUser())) {
                        Toast.makeText(getActivity(), "Your Email has not been verified", Toast.LENGTH_SHORT).show();
                        logout();
                        Timber.d("USerNotVerified");
                    } else {
                        Timber.d("UserIsVerified");
                        Toast.makeText(getActivity(), "Sing in successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Sign in was not successful", Toast.LENGTH_SHORT).show();
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
}
