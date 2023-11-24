package com.example.letschat.modelUltil;

import com.google.firebase.StartupTime;
import com.google.firebase.Timestamp;

public class UserModel {
    private String username;
    private String phoneNumber;
    private Timestamp timestamp;
    private String userId;
    private String FCMToken;

    public UserModel() {
    }

    public UserModel(String username, String phoneNumber, Timestamp timestamp, String userId) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.timestamp = timestamp;
        this.userId =userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    public String getFCMToken() {
        return FCMToken;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }
}
