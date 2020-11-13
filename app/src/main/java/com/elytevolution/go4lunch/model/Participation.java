package com.elytevolution.go4lunch.model;

import java.util.List;

public class Participation {

    private String idPlace;

    private String namePlace;

    private List<String> uid;

    public Participation(String idPlace, String namePlace, List<String> uid){
        this.idPlace = idPlace;
        this.namePlace = namePlace;
        this.uid = uid;
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
}
