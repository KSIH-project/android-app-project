package com.project.ksih_android.ui.startup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;

public class StartupViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int startingRow = 0;
    private int rowsToLoad = 0;
    private boolean allLoaded = false;

    public StartupViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}