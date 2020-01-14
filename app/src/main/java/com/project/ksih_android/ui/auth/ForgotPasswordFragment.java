package com.project.ksih_android.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentForgotPasswordBinding;
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

public class ForgotPasswordFragment extends Fragment {

    private ForgotPasswordViewModel mViewModel;
    private FirebaseAuth mAuth;
    private FragmentForgotPasswordBinding mForgotPasswordBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        return setUpBindings(savedInstanceState, inflater, container);
    }

    private View setUpBindings(Bundle savedInstanceState, LayoutInflater inflater, ViewGroup container) {
        mForgotPasswordBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false);
        onBackPressed(mForgotPasswordBinding.forgotPasswordToolbar);
        mViewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel.class);
        if (savedInstanceState == null) {
            mViewModel.init();
        }
        mForgotPasswordBinding.setForgotPassword(mViewModel);
        setUpButtonClick();
        return mForgotPasswordBinding.getRoot();
    }

    private void setUpButtonClick() {
        mViewModel.getButtonClick().observe(this, new Observer<ForgotPasswordField>() {
            @Override
            public void onChanged(ForgotPasswordField authFields) {
                startProgressBar(mForgotPasswordBinding.progressBar);
                hideButton(mForgotPasswordBinding.buttonResetPassword);
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
                    stopProgressBar(mForgotPasswordBinding.progressBar);
                    showButton(mForgotPasswordBinding.buttonResetPassword);
                    navigateToLoginFragment(mForgotPasswordBinding.forgotPasswordToolbar);
                } else {
                    Toast.makeText(requireContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    stopProgressBar(mForgotPasswordBinding.progressBar);
                    showButton(mForgotPasswordBinding.buttonResetPassword);
                    Timber.d(task.getException().getLocalizedMessage());
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
