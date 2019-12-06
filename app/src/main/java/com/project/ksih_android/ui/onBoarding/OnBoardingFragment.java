package com.project.ksih_android.ui.onBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.project.ksih_android.R;
import com.project.ksih_android.storage.SharedPreferencesStorage;
import com.project.ksih_android.utility.Constants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

/**
 * Created by SegunFrancis
 */
public class OnBoardingFragment extends Fragment {

    private OnBoardingViewModel mOnBoardingViewModel;
    private MaterialButton nextButton;
    private SharedPreferencesStorage mStorage;
    private NavController mNavController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_on_boarding, container, false);
        mStorage = new SharedPreferencesStorage(root.getContext());
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextButton = view.findViewById(R.id.button_next);
        mOnBoardingViewModel = ViewModelProviders.of(this).get(OnBoardingViewModel.class);
        mNavController = Navigation.findNavController(view);
        mOnBoardingViewModel.seenOnBoardingScreen(mStorage.hasSeenOnBoardingScreen(Constants.ON_BOARDING_KEY));
        mOnBoardingViewModel.mUserStatesMutableLiveData.observe(this, new Observer<OnBoardingViewModel.UserStates>() {
            @Override
            public void onChanged(OnBoardingViewModel.UserStates userStates) {
                switch (userStates) {
                    case SEEN_ON_BOARDING_SCREEN:
                        /* TODO: Replace the R.id.loginFragment with R.id.navigation_home*/
                        mNavController.navigate(R.id.loginFragment);
                        break;
                    case NOT_SEEN_ON_BOARDING_SCREEN:
                        // Display OnBoarding Screen
                        break;
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnBoardingViewModel.seenOnBoardingScreen(true);
                mStorage.setSeenOnBoardingScreen(Constants.ON_BOARDING_KEY, true);
                /* TODO: Replace the R.id.loginFragment with R.id.navigation_home*/
                mNavController.navigate(R.id.loginFragment);
            }
        });
    }
}
