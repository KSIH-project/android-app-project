package com.project.ksih_android.ui.ksihrules;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.ksih_android.R;
import com.project.ksih_android.ui.member.MemberViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class KSIHRules extends Fragment {

    private KSIHRulesViewModel mKSIHRulesViewModel;

    public KSIHRules() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mKSIHRulesViewModel =
                ViewModelProviders.of(this).get(KSIHRulesViewModel.class);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_ksihrules, container, false);
        final TextView textView = root.findViewById(R.id.text_rules);
        mKSIHRulesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}
