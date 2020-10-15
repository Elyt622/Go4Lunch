package com.elytevolution.go4lunch.model;

public class User {

    private int id;

    private String name;

    private String surname;

    private String email;

    private String picture;

    public User(int id, String name, String surname, String email, String picture) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPicture() {
        return picture;
    }
}
