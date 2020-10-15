package com.elytevolution.go4lunch.service;

import com.elytevolution.go4lunch.model.Restaurant;

import java.util.List;

public class DummyRestaurantApiService implements RestaurantApiService{
    private List<Restaurant> restaurants = DummyRestaurantGenerator.generateRestaurant();

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }
}
