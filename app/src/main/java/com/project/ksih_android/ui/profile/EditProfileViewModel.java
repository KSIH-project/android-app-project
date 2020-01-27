package com.project.ksih_android.ui.profile;

import com.project.ksih_android.data.User;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by SegunFrancis
 */
public class EditProfileViewModel extends ViewModel {
    MutableLiveData<User> preChangedData = new MutableLiveData<>();
    MutableLiveData<User> postChangedData = new MutableLiveData<>();

    public boolean hasChangesBeenMade() {
        return preChangedData == postChangedData;
    }
}
