package com.project.ksih_android.data;

public class AboutDevelopersData {
    private int developersImageUri;
    private String textName;
    private String textProfession;
    private String email;
    private String phoneNumber;
    private String FacebookProfile;
    private String TwitterProfile;
    private String githubProfile;
    private String linkedinProfile;
    private String meduimProfile;

    public AboutDevelopersData(){

    }

    public AboutDevelopersData(String textName, String textProfession, String email, String phoneNumber, String facebookProfile, String twitterProfile, String githubProfile, String linkedinProfile, String meduimProfile) {
        this.textName = textName;
        this.textProfession = textProfession;
        this.email = email;
        this.phoneNumber = phoneNumber;
        FacebookProfile = facebookProfile;
        TwitterProfile = twitterProfile;
        this.githubProfile = githubProfile;
        this.linkedinProfile = linkedinProfile;
        this.meduimProfile = meduimProfile;
    }

    public int getDevelopersImageUri() {
        return developersImageUri;
    }

    public void setDevelopersImageUri(int developersImageUri) {
        this.developersImageUri = developersImageUri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFacebookProfile() {
        return FacebookProfile;
    }

    public void setFacebookProfile(String facebookProfile) {
        FacebookProfile = facebookProfile;
    }

    public String getTwitterProfile() {
        return TwitterProfile;
    }

    public void setTwitterProfile(String twitterProfile) {
        TwitterProfile = twitterProfile;
    }

    public String getGithubProfile() {
        return githubProfile;
    }

    public void setGithubProfile(String githubProfile) {
        this.githubProfile = githubProfile;
    }

    public String getLinkedinProfile() {
        return linkedinProfile;
    }

    public void setLinkedinProfile(String linkedinProfile) {
        this.linkedinProfile = linkedinProfile;
    }

    public String getMeduimProfile() {
        return meduimProfile;
    }

    public void setMeduimProfile(String meduimProfile) {
        this.meduimProfile = meduimProfile;
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    public String getTextProfession() {
        return textProfession;
    }

    public void setTextProfession(String textProfession) {
        this.textProfession = textProfession;
    }
}
