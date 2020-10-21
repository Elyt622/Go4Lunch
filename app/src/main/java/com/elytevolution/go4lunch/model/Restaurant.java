package com.elytevolution.go4lunch.model;

import com.google.android.gms.maps.model.LatLng;

import java.net.URL;
import java.util.List;

import androidx.annotation.Nullable;

public class Restaurant {

    private String uid;

    private String name;

    private String address;

    @Nullable
    private double rate;

    @Nullable
    private String urlPicture;

    private LatLng latLng;

    @Nullable
    private List<User> choiceRestaurantForUser;

    public Restaurant(String uid, String name, String address, LatLng latLng, @Nullable List<User> choiceRestaurantForUser) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.latLng = latLng;
        this.choiceRestaurantForUser = choiceRestaurantForUser;
    }

    public String getId() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getRate() {
        return rate;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    @Nullable
    public List<User> getChoiceRestaurantForUser() {
        return choiceRestaurantForUser;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
