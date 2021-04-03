package com.elytevolution.go4lunch.fakeapi;

import com.elytevolution.go4lunch.api.Go4LunchApi;
import com.elytevolution.go4lunch.api.Go4LunchLiveApi;
import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.model.User;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Go4LunchFakeApi implements Go4LunchApi {

    List<Restaurant> restaurants = new ArrayList<>();

    public void getUserList(Go4LunchLiveApi.UserListResponse userListResponse) {
        List<User> users = ModelGenerator.generateUsers();
        userListResponse.onSuccess(users);
    }

    public List<String> removeCurrentUserWithId(List<String> uIds, User currentUser){
        List<String> listUser = new ArrayList<>();
        for(String uid: uIds){
            if(!uid.equals(currentUser.getUid())){
                listUser.add(uid);
            }
        }
        return listUser;
    }

    @Override
    public List<String> removeCurrentUserWithId(List<String> uIds, FirebaseUser currentUser) {
        return null;
    }

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

    @Override
    public void getParticipants(String idPlace, ParticipantsResponse participantsResponse) {

    }

}
