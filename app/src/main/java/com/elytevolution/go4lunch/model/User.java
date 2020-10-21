package com.elytevolution.go4lunch.model;

public class User {

    private String uid;

    private String name;

    private String surname;

    private String email;

    private String urlPicture;

    public User(String uid, String name, String surname, String email, String urlPicture) {
        this.uid = uid;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.urlPicture = urlPicture;
    }

    public String getId() { return uid; }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getUrlPicture() {
        return urlPicture;
    }
}
