package com.project.ksih_android.ui.onBoarding;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by SegunFrancis
 */
public class OnBoardingViewModel extends ViewModel {
    public enum UserStates {
        SEEN_ON_BOARDING_SCREEN,
        NOT_SEEN_ON_BOARDING_SCREEN
    }

    final MutableLiveData<UserStates> mUserStatesMutableLiveData = new MutableLiveData<>();

    public OnBoardingViewModel() {
        mUserStatesMutableLiveData.setValue(UserStates.NOT_SEEN_ON_BOARDING_SCREEN);
    }

    public void seenOnBoardingScreen(boolean seenOnBoardingScreen) {
        if (!seenOnBoardingScreen) {
            mUserStatesMutableLiveData.setValue(UserStates.NOT_SEEN_ON_BOARDING_SCREEN);
        } else {
            mUserStatesMutableLiveData.setValue(UserStates.SEEN_ON_BOARDING_SCREEN);
        }
    }
}
