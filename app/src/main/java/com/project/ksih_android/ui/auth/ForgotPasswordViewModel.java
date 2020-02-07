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
public class ForgotPasswordViewModel extends ViewModel {
    private ForgotPasswordField forgotPassword;
    private View.OnFocusChangeListener onFocusEmail;
    private MutableLiveData<ForgotPasswordField> buttonClick = new MutableLiveData<>();

    void init() {
        forgotPassword = new ForgotPasswordField();
        onFocusEmail = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                TextInputEditText et = (TextInputEditText) view;
                if (et.getText().length() > 0 && !focused) {
                    forgotPassword.isEmailValid(true);
                }
            }
        };
    }

    public ForgotPasswordField getEmail() {
        return forgotPassword;
    }

    public View.OnFocusChangeListener getEmailOnFocusChangeListener() {
        return onFocusEmail;
    }

    public void onButtonClick() {
        if (forgotPassword.isValid()) {
            buttonClick.setValue(forgotPassword);
        }
    }

    public MutableLiveData<ForgotPasswordField> getButtonClick() {
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
