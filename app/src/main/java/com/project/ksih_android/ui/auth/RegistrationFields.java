package com.project.ksih_android.ui.auth;

import android.text.TextUtils;

import com.project.ksih_android.R;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

/**
 * Created by SegunFrancis
 */
public class RegistrationFields extends BaseObservable {

    private String email;
    private String password;
    private String confirmPassword;
    public ObservableField<Integer> emailError = new ObservableField<>();
    public ObservableField<Integer> passwordError = new ObservableField<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        // Notify that the valid property could have changed
        notifyPropertyChanged(BR.valid);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.valid);
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        notifyPropertyChanged(BR.valid);
    }

    @Bindable
    public boolean isValid() {
        boolean valid = isEmailValid(false);
        valid = isPasswordValid(false) && valid && isConfirmValid();
        return valid;
    }

    public boolean isConfirmValid() {
        boolean valid = isEmailValid(false);
        valid = isConfirmPasswordValid(false) && valid;
        return valid;
    }

    public boolean isEmailValid(boolean setMessage) {
        if (email != null && email.length() > 5) {
            int indexOfAt = email.indexOf("@");
            int indexOfDot = email.indexOf(".");
            if (indexOfAt > 0 && indexOfDot > indexOfAt && indexOfDot < email.length()) {
                emailError.set(null);
                return true;
            } else {
                if (setMessage) {
                    emailError.set(R.string.error_format_invalid);
                }
                return false;
            }
        }
        if (setMessage) {
            emailError.set(R.string.error_too_short_email);
        }
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

    public boolean isConfirmPasswordValid(boolean setMessage) {
        if (TextUtils.equals(password, confirmPassword)) {
            passwordError.set(null);
            return true;
        } else {
            if (setMessage) passwordError.set(R.string.error_mismatch_password);
            return false;
        }
    }
}
