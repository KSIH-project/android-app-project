package com.project.ksih_android.ui.startup;

import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

/**
 * Created by SegunFrancis
 */

public class StartupValidationField {

    private boolean startupName, founderName, email, telephone, description;

    void validateStartupName(TextInputLayout layout, CharSequence name) {
        if (name != null && name.length() > 2) {
            layout.setError(null);
            this.startupName = true;
        } else {
            layout.setError("Startup name is too short");
        }
    }

    void validateFounderName(TextInputLayout layout, CharSequence name) {
        if (name != null && name.length() > 2) {
            layout.setError(null);
            this.founderName = true;
        } else {
            layout.setError("Founder name is too short");
        }
    }

    void validateEmail(TextInputLayout layout, CharSequence email) {
        if (email != null && email.length() > 4 && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layout.setError(null);
            this.email = true;
        } else {
            layout.setError("Invalid email address");
        }
    }

    void validateTelephone(TextInputLayout layout, CharSequence phone) {
        if (phone != null && Patterns.PHONE.matcher(phone).matches() && phone.length() > 10) {
            layout.setError(null);
            this.telephone = true;
        } else {
            layout.setError("Invalid phone number");
        }
    }

    void validateDescription(TextInputLayout layout, CharSequence name) {
        if (name != null && name.length() > 20) {
            layout.setError(null);
            this.description = true;
        } else {
            layout.setError("Startup description is too short");
        }
    }

    void validateWebsite(TextInputLayout layout, CharSequence website) {
        if (website != null && website.length() > 4 && Patterns.WEB_URL.matcher(website).matches()) {
            layout.setError(null);
        } else {
            layout.setError("Invalid website format");
        }
    }

    void validateFacebookUrl(TextInputLayout layout, CharSequence url) {
        if (url != null && url.length() > 5 && Patterns.WEB_URL.matcher(url).matches()) {
            layout.setError(null);
        } else {
            layout.setError("Invalid url format");
        }
    }

    void validateTwitterUrl(TextInputLayout layout, CharSequence url) {
        if (url != null && url.length() > 5 && Patterns.WEB_URL.matcher(url).matches()) {
            layout.setError(null);
        } else {
            layout.setError("Invalid url format");
        }
    }

    boolean buttonVisibility() {
        return this.startupName && this.founderName && this.email && this.description && this.telephone;
    }
}
