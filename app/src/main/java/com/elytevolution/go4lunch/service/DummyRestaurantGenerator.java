package com.elytevolution.go4lunch.service;

import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyRestaurantGenerator {
    public static List<User> USERS = Arrays.asList(
            new User(1, "Yohan", "Test" ,"yohan.bernole@gmail.com", ""),
            new User(2, "Joseph","Test" ,"joseph.dupond@gmail.com", ""),
            new User(3, "Nathan", "Test","nathan.truc@gmail.com", ""),
            new User(4, "Céline", "Test","céline.nom@gmail.com", ""),
            new User(5, "Yann","Test" ,"yann.chose@gmail.com", ""),
            new User(6, "Charlotte","Test" ,"charlotte.truc@gmail.com", ""),
            new User(7, "John", "Test","john.doe@gmail.com", ""),
            new User(8, "Jane","Test" ,"jane.doe@gmail.com", ""),
            new User(9, "Franck", "Test","franck.email@gmail.com", "")
    );

    public static List<Restaurant> DUMMY_RESTAURANT = Arrays.asList(new Restaurant(0, "Le Crapahuteur", "131", 2, 17, 21, 150, "Français", "kl", new ArrayList<>(Arrays.asList(USERS.get(1), USERS.get(2)))),
            new Restaurant(1, "Le Crapahuteur", "131", 2, 17, 21, 150, "Français", "kl",new ArrayList<>(Arrays.asList(USERS.get(1), USERS.get(2)))),
            new Restaurant(0, "Le Crapahuteur", "131", 2, 17, 21, 150, "Français", "kl", new ArrayList<>(Arrays.asList(USERS.get(1), USERS.get(2)))),
            new Restaurant(1, "Le Crapahuteur", "131", 2, 17, 21, 150, "Français", "kl",new ArrayList<>(Arrays.asList(USERS.get(1), USERS.get(2)))),
            new Restaurant(0, "Le Crapahuteur", "131", 2, 17, 21, 150, "Français", "kl", new ArrayList<>(Arrays.asList(USERS.get(1), USERS.get(2)))),
            new Restaurant(1, "Le Crapahuteur", "131", 2, 17, 21, 150, "Français", "kl",new ArrayList<>(Arrays.asList(USERS.get(1), USERS.get(2)))),
            new Restaurant(0, "Le Crapahuteur", "131", 2, 17, 21, 150, "Français", "kl", new ArrayList<>(Arrays.asList(USERS.get(1), USERS.get(2)))),
            new Restaurant(1, "Le Crapahuteur", "131", 2, 17, 21, 150, "Français", "kl",new ArrayList<>(Arrays.asList(USERS.get(1), USERS.get(2)))));


    static List<Restaurant> generateRestaurant() {
        return new ArrayList<>(DUMMY_RESTAURANT);
    }

    static List<User> generateUsers() { return new ArrayList<>(USERS); }

}
