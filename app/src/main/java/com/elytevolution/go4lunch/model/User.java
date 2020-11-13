package com.elytevolution.go4lunch.model;

import androidx.annotation.Nullable;

public class User {

    private String uid;

    private String displayName;

    private String email;

    @Nullable private String urlPicture;

    private String idPlace;

    public User(String uid, String displayName, String email, @Nullable String urlPicture, String idPlace) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.urlPicture = urlPicture;
        this.idPlace = idPlace;
    }

    public String getUid() {
        return uid;
    }

    public String getIdPlace() {
        return idPlace;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getUrlPicture() {
        return urlPicture;
    }
}
