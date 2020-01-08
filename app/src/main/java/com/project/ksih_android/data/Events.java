package com.project.ksih_android.data;

public class Events {
    private String id;
    private String imageUrl;
    private String eventName;
    private String email;
    private String phoneNumber;
    private String eventDescription;
    private String eventType;
    private String eventRSVP;


    public Events(String imageUrl, String eventName, String email, String phoneNumber, String eventDescription, String eventType, String eventRSVP) {
        this.eventName = eventName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.eventDescription = eventDescription;
        this.eventType = eventType;
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



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }


}
