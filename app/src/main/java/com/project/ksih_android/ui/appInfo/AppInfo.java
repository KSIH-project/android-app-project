package com.project.ksih_android.ui.appInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.project.ksih_android.BuildConfig;
import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentAppInfoBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppInfo extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentAppInfoBinding fragmentAppInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_app_info, container, false);

        fragmentAppInfoBinding.toolbarAppInfo.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        fragmentAppInfoBinding.textViewAppVersion.setText("v1" + BuildConfig.VERSION_NAME);

        fragmentAppInfoBinding.textViewByKsihAndroidTeam.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_appInfo_to_fragment_ksih_android_team));

        return fragmentAppInfoBinding.getRoot();
    }
}
