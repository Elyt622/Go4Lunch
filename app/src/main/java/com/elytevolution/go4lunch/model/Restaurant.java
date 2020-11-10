package com.elytevolution.go4lunch.model;

public class Restaurant {

    private String idPlace;

    private String name;

    public Restaurant(String idPlace, String name){
        this.idPlace = idPlace;
        this.name = name;
    }

    public String getIdPlace() {
        return idPlace;
    }

    public String getName() {
        return name;
    }
}
