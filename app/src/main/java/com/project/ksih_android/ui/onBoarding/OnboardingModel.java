package com.project.ksih_android.ui.onBoarding;

public class OnboardingModel {

    private int onBoardingImage;
    private String onBoardingTextTitle;
    private String onBoardingTextDescription;
    private String onBoardingAuthor;


    public OnboardingModel(int onBoardingImage, String onBoardingTextTitle, String onBoardingTextDescription, String onBoardingAuthor) {
        this.onBoardingImage = onBoardingImage;
        this.onBoardingTextTitle = onBoardingTextTitle;
        this.onBoardingTextDescription = onBoardingTextDescription;
        this.onBoardingAuthor = onBoardingAuthor;
    }

    public int getOnBoardingImage() {
        return onBoardingImage;
    }

    public void setOnBoardingImage(int onBoardingImage) {
        this.onBoardingImage = onBoardingImage;
    }

    public String getOnBoardingTextTitle() {
        return onBoardingTextTitle;
    }

    public void setOnBoardingTextTitle(String onBoardingTextTitle) {
        this.onBoardingTextTitle = onBoardingTextTitle;
    }

    public String getOnBoardingTextDescription() {
        return onBoardingTextDescription;
    }

    public void setOnBoardingTextDescription(String onBoardingTextDescription) {
        this.onBoardingTextDescription = onBoardingTextDescription;
    }

    public String getOnBoardingAuthor() {
        return onBoardingAuthor;
    }

    public void setOnBoardingAuthor(String onBoardingAuthor) {
        this.onBoardingAuthor = onBoardingAuthor;
    }
}
