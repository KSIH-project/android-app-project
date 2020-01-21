package com.project.ksih_android.ui.appInfo;


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
import com.project.ksih_android.databinding.FragmentAppInfoBinding;

import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppInfo extends Fragment {

    private FragmentAppInfoBinding mFragmentAppInfoBinding;


    public AppInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentAppInfoBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_app_info, container, false);

        mFragmentAppInfoBinding.toolbarAppInfo.setNavigationOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

        mFragmentAppInfoBinding.textViewByKsihAndroidTeam.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_appInfo_to_fragment_ksih_android_team));

        mFragmentAppInfoBinding.ksihAppPro.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_appInfo_to_simpleFragment));

        return mFragmentAppInfoBinding.getRoot();
    }

}
