package com.elytevolution.go4lunch.model;

import androidx.annotation.Nullable;

public class User {

    private String uid;

    private String firstName;

    private String lastName;

    private String email;

    @Nullable private String urlPicture;

    public User(String uid, String firstName, String lastName, String email, @Nullable String urlPicture) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.urlPicture = urlPicture;
    }

    public String getId() { return uid; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUrlPicture() {
        return urlPicture;
    }
}
