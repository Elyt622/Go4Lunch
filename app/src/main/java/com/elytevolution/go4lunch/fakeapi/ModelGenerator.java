package com.elytevolution.go4lunch.fakeapi;

import com.elytevolution.go4lunch.model.Participation;
import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ModelGenerator {

    public static List<User> USERS = Arrays.asList(
            new User("1", "Yohan", "yohan.bernole@gmail.com", "", "1"),
            new User("2", "Joseph", "joseph.dupond@gmail.com", "", "2"),
            new User("3", "Nathan", "nathan.truc@gmail.com", "", "3"),
            new User("4", "Céline", "céline.nom@gmail.com", "", "4"),
            new User("5", "Yann", "yann.chose@gmail.com", "", "5"),
            new User("6", "Charlotte", "charlotte.truc@gmail.com", "", "6"),
            new User("7", "John", "john.doe@gmail.com", "", "7"),
            new User("8", "Jane", "jane.doe@gmail.com", "", "8"),
            new User("9", "Franck", "franck.email@gmail.com","", "9")
    );

    public static List<Participation> PARTICIPATION = Arrays.asList(
            new Participation("1", "Restaurant 1", Collections.singletonList("1")),
            new Participation("2", "Restaurant 2", Collections.singletonList("2")),
            new Participation("3", "Restaurant 3", Collections.singletonList("3")),
            new Participation("4", "Restaurant 4", Collections.singletonList("4")),
            new Participation("5", "Restaurant 5", Collections.singletonList("5")),
            new Participation("6", "Restaurant 6", Collections.singletonList("6")),
            new Participation("7", "Restaurant 7", Collections.singletonList("7")),
            new Participation("8", "Restaurant 8", Collections.singletonList("8")),
            new Participation("9", "Restaurant 9", Collections.singletonList("9"))
    );

    public static List<Restaurant> RESTAURANTS = Arrays.asList(
            new Restaurant("0", "Restaurant 0", "Address 0", true, 48.5, 3, 2, 3.7, ""),
            new Restaurant("1", "Restaurant 1", "Address 1", true, 48.5, 3, 2, 3.7, ""),
            new Restaurant("2", "Restaurant 2", "Address 2", true, 48.5, 3, 2, 3.7, ""),
            new Restaurant("3", "Restaurant 3", "Address 3", true, 48.5, 3, 2, 3.7, ""),
            new Restaurant("4", "Restaurant 4", "Address 4", true, 48.5, 3, 2, 3.7, ""),
            new Restaurant("5", "Restaurant 5", "Address 5", true, 48.5, 3, 2, 3.7, ""),
            new Restaurant("6", "Restaurant 6", "Address 6", true, 48.5, 3, 2, 3.7, ""),
            new Restaurant("7", "Restaurant 7", "Address 7", true, 48.5, 3, 2, 3.7, ""),
            new Restaurant("8", "Restaurant 8", "Address 8", true, 48.5, 3, 2, 3.7, ""),
            new Restaurant("9", "Restaurant 9", "Address 9", true, 48.5, 3, 2, 3.7, ""),
            new Restaurant("10", "Restaurant 10", "Address 10", true, 48.5, 3, 2, 3.7, ""),
            new Restaurant("11", "Restaurant 11", "Address 11", true, 48.5, 3, 2, 3.7, ""),
            new Restaurant("12", "Restaurant 12", "Address 12", true, 48.5, 3, 2, 3.7, ""),
            new Restaurant("13", "Restaurant 13", "Address 13", true, 48.5, 3, 2, 3.7, ""),
            new Restaurant("14", "Restaurant 14", "Address 14", true, 48.5, 3, 2, 3.7, ""),
            new Restaurant("15", "Restaurant 15", "Address 15", true, 48.5, 3, 2, 3.7, "")
    );

    static List<Restaurant> generateRestaurant() {
        return new ArrayList<>(RESTAURANTS);
    }

    static List<Participation> generateParticipation() {
        return new ArrayList<>(PARTICIPATION);
    }

    static List<User> generateUsers() { return new ArrayList<>(USERS); }


}
