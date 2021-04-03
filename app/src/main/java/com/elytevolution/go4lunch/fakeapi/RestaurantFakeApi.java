package com.elytevolution.go4lunch.fakeapi;

import com.elytevolution.go4lunch.api.RestaurantApi;
import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantFakeApi implements RestaurantApi {

    List<Restaurant> restaurants = new ArrayList<>();

    @Override
    public void getRestaurantList(List<NearBySearch.Results> requestNearBySearch, RestaurantListResponse restaurantListResponse) { }

    @Override
    public void getRestaurantList(RestaurantListResponse restaurantListResponse) {
        restaurants = ModelGenerator.generateRestaurant();
        restaurantListResponse.onSuccess(restaurants);
    }

    public Restaurant getRestaurantWithId(String idPlace, List<Restaurant> restaurants){
        int index;
        for (index = 0; index < restaurants.size(); index++){
            if(restaurants.get(index).getIdPlace().equals(idPlace)){
                break;
            }
        }
        return restaurants.get(index);
    }
}
