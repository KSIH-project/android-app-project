package com.project.ksih_android.ui.profile;

import android.text.TextUtils;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.project.ksih_android.data.User;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by SegunFrancis
 */

public class EditProfileViewModel extends ViewModel {
    MutableLiveData<User> preChangedData = new MutableLiveData<>();
    MutableLiveData<User> postChangedData = new MutableLiveData<>();
    public MutableLiveData<Boolean> hasChangesBeenMade = new MutableLiveData<>();
    private View.OnFocusChangeListener onFocusChanged;
    private List<User> mUserList = new ArrayList<>();

    public void hasChangesBeenMade() {
        if (preChangedData == postChangedData)
            hasChangesBeenMade.setValue(true);
        else hasChangesBeenMade.setValue(true);
    }

    void init() {
        preChangedData.observeForever(user -> {
            mUserList.add(user);
        });

        onFocusChanged = (view, focused) -> {
            TextInputEditText et = (TextInputEditText) view;
            if (TextUtils.equals(et.getText(), mUserList.get(0).user_firstName))
                hasChangesBeenMade.setValue(true);
            else hasChangesBeenMade.setValue(true);
        };
    }

    public View.OnFocusChangeListener getOnFocusChanged() {
        return onFocusChanged;
    }
}
