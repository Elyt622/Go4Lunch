package com.elytevolution.go4lunch.event;

public class PrintPlaceForUser {

    private String uid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public PrintPlaceForUser(String uid){
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
