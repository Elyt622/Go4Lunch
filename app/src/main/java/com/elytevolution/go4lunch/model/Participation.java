package com.elytevolution.go4lunch.model;

import java.util.List;

public class Participation {

    private String idPlace;

    private List<String> uid;

    public Participation(String idPlace, List<String> uid){
        this.idPlace = idPlace;
        this.uid = uid;
    }

    public String getIdPlace() {
        return idPlace;
    }

    public List<String> getUid() {
        return uid;
    }
}
