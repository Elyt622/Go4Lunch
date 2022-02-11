package com.elytevolution.go4lunch.model;

import java.util.List;

public class Participation {

    private final String idPlace;

    private final String namePlace;

    private final String addressPlace;

    private final List<String> uid;

    public Participation(String idPlace, String namePlace, List<String> uid, String addressPlace){
        this.idPlace = idPlace;
        this.namePlace = namePlace;
        this.uid = uid;
        this.addressPlace = addressPlace;
    }

    public String getNamePlace() {
        return namePlace;
    }

    public String getIdPlace() {
        return idPlace;
    }

    public List<String> getUid() {
        return uid;
    }

    public String getAddressPlace() {
        return addressPlace;
    }
}
