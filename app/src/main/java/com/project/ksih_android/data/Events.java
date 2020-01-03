package com.project.ksih_android.data;

public class Events {
    private String id;
    private String imageUrl;
    private String eventName;
    private String eventDescription;
    private String eventType;

    public Events(String id, String imageUrl, String eventName, String eventDescription, String eventType) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventType = eventType;
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
