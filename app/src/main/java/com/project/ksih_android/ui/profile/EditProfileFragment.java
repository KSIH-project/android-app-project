package com.project.ksih_android.ui.profile;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import timber.log.Timber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.ksih_android.R;
import com.project.ksih_android.data.User;
import com.project.ksih_android.databinding.FragmentEditProfileBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentEditProfileBinding profileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);
        User user = (User) getArguments().getSerializable("user_data");
        Timber.d(user.user_email);
        Timber.d(user.user_linkedInUrl);
        Timber.d(user.user_mobile);
        return profileBinding.getRoot();
    }
}
