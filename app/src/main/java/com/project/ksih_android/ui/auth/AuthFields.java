package com.project.ksih_android.ui.auth;

import com.project.ksih_android.R;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

/**
 * Created by SegunFrancis
 */
public class AuthFields extends BaseObservable {
    private String email;
    private String password;
    public ObservableField<Integer> emailError = new ObservableField<>();
    public ObservableField<Integer> passwordError = new ObservableField<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.valid);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.valid);
    }

    @Bindable
    public boolean isValid() {
        boolean valid = isEmailValid(false);
        valid = isPasswordValid(false) && valid;
        return valid;
    }

    @Bindable
    public boolean isValidEmail() {
        if (email != null && email.length() > 5) {
            int indexOfAt = email.indexOf("@");
            int indexOfDot = email.lastIndexOf(".");
            if (indexOfAt > 0 && indexOfDot > indexOfAt && indexOfDot < email.length() - 1) {
                emailError.set(null);
                return true;
            } else {
                emailError.set(R.string.error_format_invalid);
                return false;
            }
        } else {
            emailError.set(R.string.error_too_short_email);
            return false;
        }
    }

    public boolean isEmailValid(boolean setMessage) {
        if (email != null && email.length() > 5) {
            int indexOfAt = email.indexOf("@");
            int indexOfDot = email.lastIndexOf(".");
            if (indexOfAt > 0 && indexOfDot > indexOfAt && indexOfDot < email.length() - 1) {
                emailError.set(null);
                return true;
            } else {
                if (setMessage) emailError.set(R.string.error_format_invalid);
                return false;
            }
        }
        if (setMessage) emailError.set(R.string.error_too_short_email);
        return false;
    }

    public boolean isPasswordValid(boolean setMessage) {
        if (password != null && password.length() > 5) {
            passwordError.set(null);
            return true;
        } else {
            if (setMessage) passwordError.set(R.string.error_too_short_password);
            return false;
        }
    }
}
