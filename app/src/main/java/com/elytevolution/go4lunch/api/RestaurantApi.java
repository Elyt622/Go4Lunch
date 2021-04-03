package com.elytevolution.go4lunch.api;

import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.model.Restaurant;

import java.util.List;

public interface RestaurantApi {

    void getRestaurantList(List<NearBySearch.Results> requestNearBySearch, RestaurantApi.RestaurantListResponse restaurantListResponse);

    void getRestaurantList(RestaurantApi.RestaurantListResponse restaurantListResponse);

    Restaurant getRestaurantWithId(String idPlace, List<Restaurant> restaurants);

    interface RestaurantListResponse {
        void onSuccess(List<Restaurant> restaurantList);
    }
}
