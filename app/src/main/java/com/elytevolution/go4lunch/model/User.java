package com.elytevolution.go4lunch.model;

import androidx.annotation.Nullable;

public class User {

    private final String fcmToken;

    private final String uid;

    private final String displayName;

    private final String email;

    @Nullable private final String urlPicture;

    private final String idPlace;

    public User(String uid, String displayName, String email, @Nullable String urlPicture, String idPlace, String fcmToken) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.urlPicture = urlPicture;
        this.idPlace = idPlace;
        this.fcmToken = fcmToken;
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

    public String getFcmToken() {
        return fcmToken;
    }
}
