package com.project.ksih_android.ui.auth;

import com.project.ksih_android.R;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

/**
 * Created by SegunFrancis
 */
public class ForgotPasswordField extends BaseObservable {

    private String email;
    public ObservableField<Integer> emailError = new ObservableField<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.valid);
    }

    @Bindable
    public boolean isValid() {
        return isEmailValid(false);
    }

    public boolean isEmailValid(boolean setMessage) {
        if (email != null && email.length() > 5) {
            int indexOfAt = email.indexOf("@");
            int indexOfDot = email.lastIndexOf(".");
            if (indexOfAt > 0 && indexOfDot > indexOfAt && indexOfDot < email.length() - 1) {
                emailError.set(null);
                return true;
            } else {
                if (setMessage)
                    emailError.set(R.string.error_format_invalid);
                return false;
            }
        }
        if (setMessage)
            emailError.set(R.string.error_too_short_email);
        return false;
    }
}
