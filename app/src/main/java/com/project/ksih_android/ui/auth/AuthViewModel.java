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
public class AuthViewModel extends ViewModel {
    private AuthFields login;
    private View.OnFocusChangeListener onFocusEmail;
    private View.OnFocusChangeListener onFocusPassword;
    private MutableLiveData<AuthFields> buttonClick = new MutableLiveData<>();

    void init() {
        login = new AuthFields();
        onFocusEmail = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                TextInputEditText et = (TextInputEditText) view;
                if (et.getText().length() > 0 && !focused) {
                    login.isEmailValid(true);
                }
            }
        };

        onFocusPassword = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                TextInputEditText et = (TextInputEditText) view;
                if (et.getText().length() > 0 && !focused) {
                    login.isPasswordValid(true);
                }
            }
        };
    }

    void forgotPasswordInit(TextInputEditText et) {
        login = new AuthFields();
        if (et.getText().length() > 0) {
            login.isEmailValid(true);
        }
    }

    public AuthFields getLogin() {
        return login;
    }

    public View.OnFocusChangeListener getEmailOnFocusChangeListener() {
        return onFocusEmail;
    }

    public View.OnFocusChangeListener getPasswordOnFocusChangeListener() {
        return onFocusPassword;
    }

    public void onButtonClick() {
        if (login.isValid()) {
            buttonClick.setValue(login);
        }
    }

    public void onRecoveryButtonClicked() {
        if (login.isValidEmail()) {
            buttonClick.setValue(login);
        }
    }

    public MutableLiveData<AuthFields> getButtonClick() {
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
