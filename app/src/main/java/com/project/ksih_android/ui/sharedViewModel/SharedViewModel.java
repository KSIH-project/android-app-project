package com.project.ksih_android.ui.sharedViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by SegunFrancis
 */
public class SharedViewModel extends ViewModel {

    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> userEmail = new MutableLiveData<>();
    public MutableLiveData<String> userProfilePhotoUrl = new MutableLiveData<>();
}
