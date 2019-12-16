package com.project.ksih_android.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentForgotPasswordBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import timber.log.Timber;

/**
 * Created by SegunFrancis
 */
public class ForgotPasswordFragment extends Fragment {

    private AuthViewModel mViewModel;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        return setUpBindings(savedInstanceState, inflater, container);
    }

    private View setUpBindings(Bundle savedInstanceState, LayoutInflater inflater, ViewGroup container) {
        FragmentForgotPasswordBinding forgotPasswordBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false);
        onBackPressed(forgotPasswordBinding.forgotPasswordToolbar);
        mViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        if (savedInstanceState == null) {
            mViewModel.forgotPasswordInit(forgotPasswordBinding.textInputForgotPassword);
        }
        forgotPasswordBinding.setLogin(mViewModel);
        setUpButtonClick();
        return forgotPasswordBinding.getRoot();
    }

    private void setUpButtonClick() {
        mViewModel.getButtonClick().observe(this, new Observer<AuthFields>() {
            @Override
            public void onChanged(AuthFields authFields) {
                //TODO: Navigate to Login Fragment
                resetPassword(authFields.getEmail());
            }
        });
    }

    private void onBackPressed(MaterialToolbar toolbar) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });
    }

    private void resetPassword(String password) {
        mAuth.sendPasswordResetEmail(password).addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(requireContext(), "Check your email for password reset link", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    Timber.d(task.getException().getLocalizedMessage());
                }
            }
        });
    }
}
