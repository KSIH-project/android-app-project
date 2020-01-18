package com.project.ksih_android.ui.ksihrules;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentKsihrulesBinding;

import timber.log.Timber;

import static com.project.ksih_android.utility.Constants.KSIH_RULES_FIREBASE_REFERENCE;

/**
 * A simple {@link Fragment} subclass.
 */
public class KSIHRules extends Fragment {
    private FragmentKsihrulesBinding mFragmentKsihrulesBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mFragmentKsihrulesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ksihrules, container, false);
        mFragmentKsihrulesBinding.ksihRulesProgressBar.start();
        DatabaseReference databaseReferenceKsihRules = FirebaseDatabase.getInstance().getReference(KSIH_RULES_FIREBASE_REFERENCE);
        databaseReferenceKsihRules.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFragmentKsihrulesBinding.textviewKsihRules.setText(dataSnapshot.getValue(String.class));
                mFragmentKsihrulesBinding.ksihRulesProgressBar.stop();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mFragmentKsihrulesBinding.ksihRulesProgressBar.stop();
                Timber.d("Database Error: %s", databaseError.getDetails());
            }
        });
        return mFragmentKsihrulesBinding.getRoot();
    }
}
