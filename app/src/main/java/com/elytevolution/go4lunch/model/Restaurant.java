package com.elytevolution.go4lunch.model;

import androidx.annotation.Nullable;

public class Restaurant {

    private final String idPlace;

    private final String name;

    @Nullable
    private final String address;

    @Nullable
    private final Boolean currentOpen;

    private final double lgt;
    private final double lat;

    @Nullable
    private final Double rating;

    private int participation;

    @Nullable
    private final String photoRef;

    public Restaurant(String idPlace, String name, @Nullable String address, @Nullable Boolean currentOpen, double lgt, double lat, int participation, @Nullable Double rating, @Nullable String photoRef){
        this.idPlace = idPlace;
        this.name = name;
        this.address = address;
        this.currentOpen = currentOpen;
        this.lgt = lgt;
        this.lat = lat;
        this.participation = participation;
        this.rating = rating;
        this.photoRef = photoRef;
    }

    public String getIdPlace() {
        return idPlace;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public Boolean isCurrentOpen() {
        return currentOpen;
    }

    public double getLgt() {
        return lgt;
    }

    public double getLat() {
        return lat;
    }

    @Nullable
    public Double getRating() {
        return rating;
    }

    public int getParticipation() {
        return participation;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    @Nullable
    public String getPhotoRef() {
        return photoRef;
    }

    // Number of participants
    public void setParticipation(int participation) {
        this.participation = participation;
    }
}
