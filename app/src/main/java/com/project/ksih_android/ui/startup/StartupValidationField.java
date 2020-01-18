package com.project.ksih_android.ui.startup;

import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

/**
 * Created by SegunFrancis
 */

class StartupValidationField {

    private boolean startupName, founderName, email, telephone, description;
    private boolean coFounder, website, facebookUrl, twitterUrl;

    /**
     * Validates the input that the user enters as the startup name
     *
     * @param layout is the textInput layout. It's used in order to set the error message
     * @param name   is the startup name entered by the user
     */
    void validateStartupName(TextInputLayout layout, CharSequence name) {
        if (name != null && name.length() > 2) {
            layout.setError(null);
            this.startupName = true;
        } else {
            layout.setError("Startup name is too short");
        }
    }

    /**
     * Validates the input that the user enters as the startup founder name
     *
     * @param layout is the textInput layout. It's used in order to set the error message
     * @param name   is the startup founder's name entered by the user
     */
    void validateFounderName(TextInputLayout layout, CharSequence name) {
        if (name != null && name.length() > 4) {
            layout.setError(null);
            this.founderName = true;
        } else {
            layout.setError("Founder name is too short");
        }
    }

    /**
     * Validates the input that the user enters as the startup email address
     *
     * @param layout is the textInput layout. It's used in order to set the error message
     * @param email  is the startup email address entered by the user
     */
    void validateEmail(TextInputLayout layout, CharSequence email) {
        if (email != null && email.length() > 4 && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layout.setError(null);
            this.email = true;
        } else {
            layout.setError("Invalid email address");
        }
    }

    /**
     * Validates the input that the user enters as the startup contact phone number
     *
     * @param layout is the textInput layout. It's used in order to set the error message
     * @param phone  is the startup phone number entered by the user
     */
    void validateTelephone(TextInputLayout layout, CharSequence phone) {
        if (phone != null && Patterns.PHONE.matcher(phone).matches() && phone.length() > 10) {
            layout.setError(null);
            this.telephone = true;
        } else {
            layout.setError("Invalid phone number");
        }
    }

    /**
     * Validates the input that the user enters as the startup description
     *
     * @param layout      is the textInput layout. It's used in order to set the error message
     * @param description is the startup description entered by the user
     */
    void validateDescription(TextInputLayout layout, CharSequence description) {
        if (description != null && description.length() > 20) {
            layout.setError(null);
            this.description = true;
        } else {
            layout.setError("Startup description is too short");
        }
    }

    /**
     * Validates the input that the user enters as the startup's website
     *
     * @param layout  is the textInput layout. It's used in order to set the error message
     * @param website is the startup's website entered by the user
     */
    void validateWebsite(TextInputLayout layout, CharSequence website) {
        if (website != null && website.length() > 4 && Patterns.WEB_URL.matcher(website).matches()) {
            layout.setError(null);
        } else {
            layout.setError("Invalid website format");
        }
    }

    /**
     * Validates the input that the user enters as the startup's facebook url
     *
     * @param layout is the textInput layout. It's used in order to set the error message
     * @param url    is the startup facebook url entered by the user
     */
    void validateFacebookUrl(TextInputLayout layout, CharSequence url) {
        if (url != null && url.length() > 5 && Patterns.WEB_URL.matcher(url).matches()) {
            layout.setError(null);
        } else {
            layout.setError("Invalid url format");
        }
    }

    /**
     * Validates the input that the user enters as the startup's twitter url
     *
     * @param layout is the textInput layout. It's used in order to set the error message
     * @param url    is the startup twitter url entered by the user
     */
    void validateTwitterUrl(TextInputLayout layout, CharSequence url) {
        if (url != null && url.length() > 5 && Patterns.WEB_URL.matcher(url).matches()) {
            layout.setError(null);
        } else {
            layout.setError("Invalid url format");
        }
    }

    /**
     * Returns a boolean that is true when all the required fields have been filled
     */
    boolean isButtonEnabled() {
        return this.startupName && this.founderName && this.email && this.description && this.telephone;
    }

    /**
     * Returns a boolean that is true when at least one field has been filled or when an image has been selected
     */
    boolean isButtonEnabledForEdit() {
        return this.startupName || this.founderName || this.email || this.description || this.telephone;
    }
}
