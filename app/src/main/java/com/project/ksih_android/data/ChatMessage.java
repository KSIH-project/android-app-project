package com.project.ksih_android.data;

public class ChatMessage {

    private String id;
    private String text;
    private String name;
    private String photoUrl;
    private String ImageUrl;
    private String from;
    private String time;
    private User users;

    public ChatMessage(){}

    public ChatMessage( String text, String name, String photoUrl, String imageUrl, String from, String time) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        ImageUrl = imageUrl;
        this.from = from;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }
}
