package com.project.ksih_android.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_login, container, false);
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
}
