package com.project.ksih_android.ui.auth;

import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by SegunFrancis
 */
public class RegistrationViewModel extends ViewModel {
    private RegistrationFields registration;
    private View.OnFocusChangeListener onFocusEmail;
    private View.OnFocusChangeListener onFocusPassword;
    private View.OnFocusChangeListener onFocusConfirmPassword;
    private MutableLiveData<RegistrationFields> buttonClick = new MutableLiveData<>();

    void init() {
        registration = new RegistrationFields();
        onFocusEmail = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                TextInputEditText et = (TextInputEditText) view;
                if (et.getText().length() > 0 && !focused) {
                    registration.isEmailValid(true);
                }
            }
        };

        onFocusPassword = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                TextInputEditText et = (TextInputEditText) view;
                if (et.getText().length() > 0 && !focused) {
                    registration.isPasswordValid(true);
                }
            }
        };

        onFocusConfirmPassword = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                TextInputEditText et = (TextInputEditText) view;
                if (et.getText().length() > 0 && !focused) {
                    registration.isConfirmPasswordValid(true);
                }
            }
        };
    }

    public RegistrationFields getRegistration() {
        return registration;
    }

    public View.OnFocusChangeListener getEmailOnFocusChangeListener() {
        return onFocusEmail;
    }

    public View.OnFocusChangeListener getPasswordOnFocusChangeListener() {
        return onFocusPassword;
    }

    public View.OnFocusChangeListener getConfirmPasswordOnFocusChangeListener() {
        return onFocusConfirmPassword;
    }

    public void onButtonClick() {
        if (registration.isValid()) {
            buttonClick.setValue(registration);
        }
    }

    public MutableLiveData<RegistrationFields> getButtonClick() {
        return buttonClick;
    }

    @BindingAdapter("error")
    public static void setError(TextInputLayout editText, Object strOrResId) {
        if (strOrResId instanceof Integer) {
            editText.setError(editText.getContext().getString((Integer) strOrResId));
        } else {
            editText.setError((String) strOrResId);
        }
    }

    @BindingAdapter("onFocus")
    public static void bindFocusChange(TextInputEditText editText, View.OnFocusChangeListener onFocusChangeListener) {
        if (editText.getOnFocusChangeListener() == null) {
            editText.setOnFocusChangeListener(onFocusChangeListener);
        }
    }
}
