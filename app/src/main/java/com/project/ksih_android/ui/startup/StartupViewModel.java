package com.project.ksih_android.ui.startup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StartupViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Boolean> isButtonEnabled = new MutableLiveData<>();
    private StartUpField mStartUpField;

    public StartupViewModel() {
        mStartUpField = new StartUpField();
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
        isButtonEnabled.setValue(true);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Boolean> getIsButtonEnabled() {
        return isButtonEnabled;
    }

    public StartUpField getUrl() {
        return mStartUpField;
    }
}