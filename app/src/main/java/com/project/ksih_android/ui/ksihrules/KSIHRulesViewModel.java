package com.project.ksih_android.ui.ksihrules;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KSIHRulesViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public KSIHRulesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
