package com.example.poisonousking.auxiliaryclasses;

public class UserModel {
    private String username;
    private final String email_address;
    private String userId;

    public UserModel(String username, String emailAddress, String userId) {
        this.username = username;
        this.email_address = emailAddress;
        this.userId = userId;
    }
    public String getEmail() {
        return email_address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
