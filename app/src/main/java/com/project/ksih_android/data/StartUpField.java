package com.project.ksih_android.data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import static com.project.ksih_android.utility.Methods.loadUrlWithChromeTab;

/**
 * Created by SegunFrancis
 */

public class StartUpField extends BaseObservable implements Serializable {
    private String id;
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
    private String telephone;
    private String email;

    public StartUpField() {
    }

    public StartUpField(String id, String startupName, String startupDescription, String startupFounder,
                        @Nullable String startupCoFounder, @Nullable String startupWebsite, @Nullable String facebookUrl,
                        @Nullable String twitterUrl, String imageUrl, String telephone, String email) {
        this.id = id;
        this.startupName = startupName;
        this.startupDescription = startupDescription;
        this.startupFounder = startupFounder;
        this.startupCoFounder = startupCoFounder;
        this.startupWebsite = startupWebsite;
        this.facebookUrl = facebookUrl;
        this.twitterUrl = twitterUrl;
        this.imageUrl = imageUrl;
        this.telephone = telephone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void dialIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        Uri uri = Uri.parse("tel:" + getTelephone());
        intent.setData(uri);
        context.startActivity(intent);
    }

    public void emailIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.fromParts("mailto", getEmail(), null));
        context.startActivity(Intent.createChooser(intent, "Select Email App"));
    }

    public void websiteIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (getFacebookUrl().startsWith("http") || getStartupWebsite().startsWith("https")) {
            intent.setData(Uri.parse(getStartupWebsite()));
        } else {
            String formattedUrl = "http://" + getStartupWebsite();
            intent.setData(Uri.parse(formattedUrl));
        }
        context.startActivity(intent);
    }

    public void facebookIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (getFacebookUrl().startsWith("http") || getFacebookUrl().startsWith("https")) {
            intent.setData(Uri.parse(getFacebookUrl()));
        } else {
            String formattedUrl = "http://" + getFacebookUrl();
            intent.setData(Uri.parse(formattedUrl));
        }
        context.startActivity(intent);
    }

    public void twitterIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (getTwitterUrl().startsWith("http") || getTwitterUrl().startsWith("https")) {
            intent.setData(Uri.parse(getTwitterUrl()));
        } else {
            String formattedUrl = "http://" + getTwitterUrl();
            intent.setData(Uri.parse(formattedUrl));
        }
        context.startActivity(intent);
    }
}
