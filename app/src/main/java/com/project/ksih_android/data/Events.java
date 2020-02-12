package com.project.ksih_android.data;

import android.os.Parcel;
import android.os.Parcelable;


public class Events implements Parcelable {
    private String id;
    private String eventName;
    private String email;
    private String phoneNumber;
    private String eventDescription;
    private String eventLocation;
    private String eventRSVP;
    private String imageUrl = "";
    private String date;
    private String time;

    public Events() {

    }


    public static final Creator<Events> CREATOR = new Creator<Events>() {
        @Override
        public Events createFromParcel(Parcel in) {
            return new Events(in);
        }

        @Override
        public Events[] newArray(int size) {
            return new Events[size];
        }
    };

    public Events(String id, String imageUrl, String eventName, String eventLocation, String email, String phoneNumber, String date,
                  String time, String eventDescription, String eventRSVP) {
        this.imageUrl = imageUrl;
        this.eventName = eventName;
        this.email = email;
        this.eventLocation = eventLocation;
        this.phoneNumber = phoneNumber;
        this.eventDescription = eventDescription;
        this.eventRSVP = eventRSVP;
        this.id = id;
        this.time = time;
        this.date = date;
    }

    private Events(Parcel in) {
        id = in.readString();
        imageUrl = in.readString();
        eventName = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        eventDescription = in.readString();
        eventRSVP = in.readString();
        date = in.readString();
        eventLocation = in.readString();
        time = in.readString();
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

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
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

    public String getEventRSVP() {
        return eventRSVP;
    }

    public void setEventRSVP(String eventRSVP) {
        this.eventRSVP = eventRSVP;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imageUrl);
        dest.writeString(eventName);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeString(eventDescription);
        dest.writeString(eventRSVP);
        dest.writeString(date);
        dest.writeString(time);
    }
}
