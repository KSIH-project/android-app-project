package com.project.ksih_android.data;

public class AboutDevelopersData {
    private int developersImage;
    private String textName;
    private String textProfession;

    public AboutDevelopersData(){

    }

    public AboutDevelopersData(int developersImage, String textName, String textProfession) {
        this.developersImage = developersImage;
        this.textName = textName;
        this.textProfession = textProfession;
    }

    public int getDevelopersImage() {
        return developersImage;
    }

    public void setDevelopersImage(int developersImage) {
        this.developersImage = developersImage;
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
