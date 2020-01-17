package com.project.ksih_android.ui.startup;

import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

/**
 * Created by SegunFrancis
 */
public class StartupValidationField extends BaseObservable {
    private String texts;
    private String url;
    public ObservableField<String> error = new ObservableField<>();

    public String getTexts() {
        return texts;
    }

    public void setTexts(String texts) {
        this.texts = texts;
        notifyPropertyChanged(BR.valid);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.valid);
    }

    boolean isTextValid(boolean setMessage) {
        if (texts != null && texts.length() > 2) {
            error.set(null);
            return true;
        } else {
            if (setMessage)
                error.set("Invalid Input");
            return false;
        }
    }

    boolean isUrlValid(boolean setMessage) {
        if (url != null && Patterns.WEB_URL.matcher(url).matches()) {
            error.set(null);
            return true;
        } else {
            if (setMessage)
                error.set("Invalid URL format");
            return false;
        }
    }
}
