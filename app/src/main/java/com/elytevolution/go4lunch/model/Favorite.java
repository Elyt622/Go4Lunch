package com.elytevolution.go4lunch.model;

import java.util.List;

public class Favorite {

    private final String uid;

    private final List<String> idPlace;

    public Favorite(List<String> idPlace, String uid) {
        this.idPlace = idPlace;
        this.uid = uid;
    }

    public List<String> getIdPlace() {
        return idPlace;
    }

    public String getUid() {
        return uid;
    }
}
