package com.elytevolution.go4lunch.api;

import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantLiveApi implements RestaurantApi {

    private static final String TAG = "RESTAURANT_LIVE_API";

    public void getRestaurantList(List<NearBySearch.Results> requestNearBySearch, RestaurantApi.RestaurantListResponse restaurantListResponse) {

        List<Restaurant> restaurants = new ArrayList<>();

        String idPlace, name, address, photoRef;
        Boolean currentOpen;
        double rating, longitude, latitude;

        for (NearBySearch.Results result : requestNearBySearch) {
            idPlace = result.getPlace_id();
            name = result.getName();
            currentOpen = result.getOpening_hours() == null ? null : result.getOpening_hours().getOpen_now();
            address = result.getVicinity();
            photoRef = result.getPhotos() == null ? null : result.getPhotos().get(0).getPhoto_reference();
            rating = result.getRating();
            longitude = result.getGeometry().getLocation().getLng();
            latitude = result.getGeometry().getLocation().getLat();

            restaurants.add(new Restaurant(idPlace, name, address, currentOpen, longitude,
                    latitude, 0, rating, photoRef));
        }
        restaurantListResponse.onSuccess(restaurants);
    }

    @Override
    public void getRestaurantList(RestaurantListResponse restaurantListResponse) { }

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