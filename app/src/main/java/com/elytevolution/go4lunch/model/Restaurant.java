package com.elytevolution.go4lunch.model;

import java.util.List;

public class Restaurant {

    private int id;

    private String name;

    private String address;

    private int rate;

    private int open;

    private int close;

    private int distance;

    private String type;

    private String picture;

    private List<User> choiceRestaurantForUser;

    public Restaurant(int id, String name, String address, int rate, int open, int close, int distance, String type, String picture, List<User> choiceRestaurantForUser) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rate = rate;
        this.open = open;
        this.close = close;
        this.distance = distance;
        this.type = type;
        this.picture = picture;
        this.choiceRestaurantForUser = choiceRestaurantForUser;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getRate() {
        return rate;
    }

    public int getOpen() {
        return open;
    }

    public int getClose() {
        return close;
    }

    public int getDistance() {
        return distance;
    }

    public String getType() {
        return type;
    }

    public String getPicture() {
        return picture;
    }

    public List<User> getChoiceRestaurantForUser() {
        return choiceRestaurantForUser;
    }
}
