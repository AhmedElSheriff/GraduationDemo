package com.example.android.graduationdemo.data;

/**
 * Created by Abshafi on 1/24/2017.
 */

public class User {

    private String userEmail;
    private String userPassword;
    private String userName;
    private String phoneNumber;
    private String userGender;
    private String isDriver;

    public void setIsDriver(String isUser) {
        this.isDriver = isUser;
    }

    public String getIsDriver() {
        return isDriver;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }


    public String getUserPassword() {
        return userPassword;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserGender() {
        return userGender;
    }
}
