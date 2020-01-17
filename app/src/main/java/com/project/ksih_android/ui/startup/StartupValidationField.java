package com.project.ksih_android.ui.startup;

import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

/**
 * Created by SegunFrancis
 */

public class StartupValidationField extends BaseObservable {
    private String startupName;
    private String startupDescription;
    private String startupFounder;
    private String startupEmail;
    private String startupPhone;
    private String startupWebsite;
    private String startupFacebook;
    private String startupTwitter;
    public ObservableField<String> startupNameError = new ObservableField<>();
    public ObservableField<String> startupDescriptionError = new ObservableField<>();
    public ObservableField<String> startupFounderError = new ObservableField<>();
    public ObservableField<String> startupEmailError = new ObservableField<>();
    public ObservableField<String> startupPhoneError = new ObservableField<>();
    public ObservableField<String> startupWebsiteError = new ObservableField<>();
    public ObservableField<String> startupFacebookError = new ObservableField<>();
    public ObservableField<String> startupTwitterError = new ObservableField<>();

    public String getStartupName() {
        return startupName;
    }

    public void setStartupName(String startupName) {
        this.startupName = startupName;
        notifyPropertyChanged(BR.valid);
    }

    public String getStartupDescription() {
        return startupDescription;
    }

    public void setStartupDescription(String startupDescription) {
        this.startupDescription = startupDescription;
        notifyPropertyChanged(BR.valid);
    }

    public String getStartupFounder() {
        return startupFounder;
    }

    public void setStartupFounder(String startupFounder) {
        this.startupFounder = startupFounder;
        notifyPropertyChanged(BR.valid);
    }

    public String getStartupEmail() {
        return startupEmail;
    }

    public void setStartupEmail(String startupEmail) {
        this.startupEmail = startupEmail;
        notifyPropertyChanged(BR.valid);
    }

    public String getStartupPhone() {
        return startupPhone;
    }

    public void setStartupPhone(String startupPhone) {
        this.startupPhone = startupPhone;
        notifyPropertyChanged(BR.valid);
    }

    public String getStartupWebsite() {
        return startupWebsite;
    }

    public void setStartupWebsite(String startupWebsite) {
        this.startupWebsite = startupWebsite;
        notifyPropertyChanged(BR.valid);
    }

    public String getStartupFacebook() {
        return startupFacebook;
    }

    public void setStartupFacebook(String startupFacebook) {
        this.startupFacebook = startupFacebook;
        notifyPropertyChanged(BR.valid);
    }

    public String getStartupTwitter() {
        return startupTwitter;
    }

    public void setStartupTwitter(String startupTwitter) {
        this.startupTwitter = startupTwitter;
        notifyPropertyChanged(BR.valid);
    }

    boolean isStartupNameValid() {
        if (startupName != null && startupName.length() > 2) {
            startupNameError.set(null);
            return true;
        } else {
            startupNameError.set("Startup name is too short");
            return false;
        }
    }

    boolean isStartupDescriptionValid() {
        if (startupDescription != null && startupDescription.length() > 20) {
            startupDescriptionError.set(null);
            return true;
        } else {
            startupDescriptionError.set("Startup description is too short");
            return false;
        }
    }

    boolean isStartupFounderValid() {
        if (startupFounder != null && startupFounder.length() > 2) {
            startupFounderError.set(null);
            return true;
        } else {
            startupFounderError.set("Startup founder name is too short");
            return false;
        }
    }

    boolean isStartupEmailValid(boolean setMessage) {
        if (startupEmail != null && startupEmail.length() > 5) {
            if (startupEmail != null && Patterns.EMAIL_ADDRESS.matcher(startupEmail).matches()) {
                startupEmailError.set(null);
                return true;
            } else {
                if (setMessage)
                    startupEmailError.set("Invalid email format");
                return false;
            }
        }
        if (setMessage) startupEmailError.set("Email is too short");
        return false;
    }

    boolean isStartupPhoneValid() {
        if (startupPhone.length() > 10 && Patterns.PHONE.matcher(startupPhone).matches()) {
            startupPhoneError.set(null);
            return true;
        } else {
            startupPhoneError.set("Invalid phone number");
            return false;
        }
    }

    boolean isWebsiteValid() {
        if (startupWebsite != null && Patterns.WEB_URL.matcher(startupWebsite).matches()) {
            startupWebsiteError.set(null);
            return true;
        } else {
            startupWebsiteError.set("Invalid URL format");
            return false;
        }
    }

    boolean isFacebookValid() {
        if (startupFacebook != null && Patterns.WEB_URL.matcher(startupFacebook).matches()) {
            startupFacebookError.set(null);
            return true;
        } else {
            startupFacebookError.set("Invalid URL format");
            return false;
        }
    }

    boolean isTwitterValid() {
        if (startupTwitter != null && Patterns.WEB_URL.matcher(startupTwitter).matches()) {
            startupTwitterError.set(null);
            return true;
        } else {
            startupTwitterError.set("Invalid URL format");
            return false;
        }
    }
}
