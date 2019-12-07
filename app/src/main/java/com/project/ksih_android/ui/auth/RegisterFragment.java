package com.project.ksih_android.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentRegisterBinding;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * Created by SegunFrancis
 */
public class RegisterFragment extends Fragment {
    private AuthViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_register, container, false);
        return setUpBindings(savedInstanceState, inflater, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private View setUpBindings(Bundle savedInstanceState, LayoutInflater inflater, ViewGroup container) {
        FragmentRegisterBinding registerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        registerBinding.signUpToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });
        mViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        if (savedInstanceState == null) {
            mViewModel.init();
        }
        registerBinding.setLogin(mViewModel);
        setUpButtonClick();
        return registerBinding.getRoot();
    }

    private void setUpButtonClick() {
        mViewModel.getButtonClick().observe(this, new Observer<AuthFields>() {
            @Override
            public void onChanged(AuthFields authFields) {
                //TODO: Navigate to Login Fragment
            }
        });
    }
}
