package com.project.ksih_android.ui.startup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.project.ksih_android.R;
import com.project.ksih_android.ui.AuthActivity;

public class StartupFragment extends Fragment {

    private StartupViewModel mStartupViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mStartupViewModel =
                ViewModelProviders.of(this).get(StartupViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_startup, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        mStartupViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        // TODO: Remove temporary navigation
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), AuthActivity.class));
            }
        });
        return root;
    }
}