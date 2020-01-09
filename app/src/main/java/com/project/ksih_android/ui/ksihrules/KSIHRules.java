package com.project.ksih_android.ui.ksihrules;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentKsihrulesBinding;
import com.project.ksih_android.ui.member.MemberViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class KSIHRules extends Fragment {
    private FragmentKsihrulesBinding mFragmentKsihrulesBinding;


    private KSIHRulesViewModel mKSIHRulesViewModel;
    private String ksihRules;
    private FirebaseDatabase mFirebaseDatabaseKsihRules;
    private DatabaseReference mDatabaseReferenceKsihRules;


    public KSIHRules() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mKSIHRulesViewModel = ViewModelProviders.of(this).get(KSIHRulesViewModel.class);
        // Inflate the layout for this fragment
        mFragmentKsihrulesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ksihrules, container, false);
        mDatabaseReferenceKsihRules = mFirebaseDatabaseKsihRules.getReference("ksih_rules");
        mDatabaseReferenceKsihRules.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mKSIHRulesViewModel.getText().observe(this, s -> mFragmentKsihrulesBinding.textviewKsihRules.setText("Ksih rules"));
        return mFragmentKsihrulesBinding.getRoot();
    }

}
