package com.elytevolution.go4lunch.fakeapi;

import com.elytevolution.go4lunch.api.Go4LunchApi;
import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;

public class Go4LunchFakeApi implements Go4LunchApi {

    public void getUserList(Go4LunchApi.UserListResponse userListResponse) {
        List<User> users = ModelGenerator.generateUsers();
        userListResponse.onSuccess(users);
    }

    public List<String> removeCurrentUserWithId(List<String> uIds, String currentUserId){
        List<String> listUser = new ArrayList<>();
        for(String uid: uIds){
            if(!uid.equals(currentUserId)){
                listUser.add(uid);
            }
        }
        return listUser;
    }

    @Override
    public void getRestaurantList(List<NearBySearch.Results> requestNearBySearch, RestaurantListResponse restaurantListResponse) {
        List<Restaurant> restaurants = ModelGenerator.generateRestaurant();
        restaurantListResponse.onSuccess(restaurants);
    }

    @Override
    public void getParticipants(String idPlace, ParticipantsResponse participantsResponse) { }
}
