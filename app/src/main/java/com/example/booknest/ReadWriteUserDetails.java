package com.example.booknest;

import android.widget.EditText;

public class ReadWriteUserDetails {
    public String username;
    public String email;
    public String address;

    public String password;

    public String photoURL;

    public Boolean isAdmin;

    public ReadWriteUserDetails(){}
    public ReadWriteUserDetails(String username, String email, String address, String password) {
        this.username = username;
        this.email = email;
        this.address = address;
        this.password = password;
    }


    public ReadWriteUserDetails(String username, String email, String address, String password, String photoURL) {
        this.username = username;
        this.email = email;
        this.address = address;
        this.password = password;
        this.photoURL = photoURL;
        this.isAdmin = Boolean.FALSE;

    }
    public ReadWriteUserDetails(String username, String email, String address, String password, Boolean isAdmin) {
        this.username = username;
        this.email = email;
        this.address = address;
        this.password = password;
        this.photoURL = photoURL;
        this.isAdmin = isAdmin;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

