package com.elytevolution.go4lunch.service;

import com.elytevolution.go4lunch.model.User;

import java.util.Arrays;
import java.util.List;

public class DummyUsersGenerator {
    public static List<User> USERS = Arrays.asList(
            new User("1", "Yohan", "Test" ,"yohan.bernole@gmail.com", ""),
            new User("2", "Joseph","Test" ,"joseph.dupond@gmail.com", ""),
            new User("3", "Nathan", "Test","nathan.truc@gmail.com", ""),
            new User("4", "Céline", "Test","céline.nom@gmail.com", ""),
            new User("5", "Yann","Test" ,"yann.chose@gmail.com", ""),
            new User("6", "Charlotte","Test" ,"charlotte.truc@gmail.com", ""),
            new User("7", "John", "Test","john.doe@gmail.com", ""),
            new User("8", "Jane","Test" ,"jane.doe@gmail.com", ""),
            new User("9", "Franck", "Test","franck.email@gmail.com", "")
    );
}
