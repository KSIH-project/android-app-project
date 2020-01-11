package com.project.ksih_android.ui.others;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentAboutKsihBinding;

import static com.project.ksih_android.utility.Constants.ABOUT_KSIH_FIREBASE_REFERENCE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutKsihFragment extends Fragment {
    private FragmentAboutKsihBinding mFragmentAboutKsihBinding;

    private String aboutKsih;
    private DatabaseReference mDatabaseReferenceAboutKsih;


    public AboutKsihFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentAboutKsihBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_ksih, container, false);
        mFragmentAboutKsihBinding.progressBarAbout.start();
        mFragmentAboutKsihBinding.toolbarAboutKsih.setNavigationOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());
        mDatabaseReferenceAboutKsih = FirebaseDatabase.getInstance().getReference(ABOUT_KSIH_FIREBASE_REFERENCE);
        mDatabaseReferenceAboutKsih.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFragmentAboutKsihBinding.textViewAboutKsih.setText(dataSnapshot.getValue(String.class));
                mFragmentAboutKsihBinding.progressBarAbout.stop();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mFragmentAboutKsihBinding.progressBarAbout.stop();
            }
        });

        return mFragmentAboutKsihBinding.getRoot();
    }

}
