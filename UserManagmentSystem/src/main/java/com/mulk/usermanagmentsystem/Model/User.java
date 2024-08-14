package com.mulk.usermanagmentsystem.Model;

public class User {
    private int userId;
    private String fullName;
    private String email;
    private String country;

    public User(int userId, String fullName, String email, String country) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.country = country;
    }

    public User(String fullName, String email, String country) {
        this.fullName = fullName;
        this.email = email;
        this.country = country;
    }

    public User(int userId) {
        this.userId = userId;
    }

    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
