package com.elytevolution.go4lunch.di;

import com.elytevolution.go4lunch.service.RestaurantApiService;
import com.elytevolution.go4lunch.service.DummyRestaurantApiService;

public class DI {
    private static DummyRestaurantApiService service = new DummyRestaurantApiService();

    public static DummyRestaurantApiService getRestaurantApiService(){
        return service;
    }

    public static RestaurantApiService getNewInstanceApiService(){
        return new DummyRestaurantApiService();
    }
}
