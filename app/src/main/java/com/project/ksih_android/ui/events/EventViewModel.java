package com.project.ksih_android.ui.events;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by ChukwuwaUchenna
 */

public class EventViewModel extends ViewModel {

    private MutableLiveData<String> mEventTopic;
    private MutableLiveData<String> mEventDescription;
    private MutableLiveData<String> mEventType;

    public EventViewModel() {
        mEventTopic = new MutableLiveData<>();
        mEventTopic.setValue("Dev c training with Andela");
        mEventDescription = new MutableLiveData<>();
        mEventDescription.setValue("A Training that helps Android Developers on their journey");
        mEventType = new MutableLiveData<>();
        mEventType.setValue("Android");

    }

    public LiveData<String> getText() {
        return mEventTopic;
    }

}

