package com.project.ksih_android.ui.startup;

import android.util.Patterns;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

/**
 * Created by SegunFrancis
 */
public class StartUpField extends BaseObservable {
    private String url;
    private String startupName;
    private String startupDescription;
    private String startupFounder;
    @Nullable
    private String startupCoFounder;
    @Nullable
    private String startupWebsite;
    @Nullable
    private String facebookUrl;
    @Nullable
    private String twitterUrl;
    private String imageUrl;

    public StartUpField() {
    }

    public StartUpField(String startupName, String startupDescription, String startupFounder,
                        String startupCoFounder, String startupWebsite, String facebookUrl,
                        String twitterUrl, String imageUrl) {
        this.startupName = startupName;
        this.startupDescription = startupDescription;
        this.startupFounder = startupFounder;
        this.startupCoFounder = startupCoFounder;
        this.startupWebsite = startupWebsite;
        this.facebookUrl = facebookUrl;
        this.twitterUrl = twitterUrl;
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.valid);
    }


    public boolean isUrlValid() {
        //return Patterns.WEB_URL.matcher(url).matches();
        return true;
    }

    public String getStartupName() {
        return startupName;
    }

    public void setStartupName(String startupName) {
        this.startupName = startupName;
    }

    public String getStartupDescription() {
        return startupDescription;
    }

    public void setStartupDescription(String startupDescription) {
        this.startupDescription = startupDescription;
    }

    public String getStartupFounder() {
        return startupFounder;
    }

    public void setStartupFounder(String startupFounder) {
        this.startupFounder = startupFounder;
    }

    public String getStartupCoFounder() {
        return startupCoFounder;
    }

    public void setStartupCoFounder(@Nullable String startupCoFounder) {
        this.startupCoFounder = startupCoFounder;
    }

    public String getStartupWebsite() {
        return startupWebsite;
    }

    public void setStartupWebsite(@Nullable String startupWebsite) {
        this.startupWebsite = startupWebsite;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(@Nullable String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(@Nullable String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
