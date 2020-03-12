package com.project.ksih_android.data;

import java.io.Serializable;

/**
 * Created by SegunFrancis
 */
public class User implements Serializable {
    public String user_name;
    public String user_firstName;
    public String user_lastName;
    public String user_email;
    public String user_mobile;
    public String user_stack;
    public String user_linkedInUrl;
    public String user_githubUrl;
    public String user_mediumUrl;
    public String user_facebookUrl;
    public String user_twitterUrl;
    public String user_image;
    public String user_id;

    public User() {
    }

    public User(String user_name, String user_firstName, String user_lastName, String user_email, String user_mobile,
                String user_stack, String user_linkedInUrl, String user_githubUrl, String user_mediumUrl,
                String user_facebookUrl, String user_twitterUrl, String user_image, String user_id) {
        this.user_name = user_name;
        this.user_firstName = user_firstName;
        this.user_lastName = user_lastName;
        this.user_email = user_email;
        this.user_mobile = user_mobile;
        this.user_stack = user_stack;
        this.user_linkedInUrl = user_linkedInUrl;
        this.user_githubUrl = user_githubUrl;
        this.user_mediumUrl = user_mediumUrl;
        this.user_facebookUrl = user_facebookUrl;
        this.user_twitterUrl = user_twitterUrl;
        this.user_image = user_image;
        this.user_id = user_id;
    }
}
