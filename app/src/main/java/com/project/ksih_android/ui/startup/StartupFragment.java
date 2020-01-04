package com.project.ksih_android.ui.startup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentStartupBinding;

public class StartupFragment extends Fragment {

    private StartupViewModel mStartupViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mStartupViewModel =
                ViewModelProviders.of(this).get(StartupViewModel.class);
        mStartupViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return setUpBinding(inflater, container);
    }

    private View setUpBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentStartupBinding startupBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_startup, container, false);
        startupBinding.tempFab.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_navigation_startup_to_addStartUpFragment));
        return startupBinding.getRoot();
    }
}