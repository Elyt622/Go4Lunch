package com.elytevolution.go4lunch.api;

import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.model.User;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public interface Go4LunchApi {

     //Users

     //WorkmatePresenter function

     void getUserList(Go4LunchLiveApi.UserListResponse userListResponse);

     //DetailsPresenter function

     List<String> removeCurrentUserWithId(List<String> uIds, FirebaseUser currentUser);

     List<String> removeCurrentUserWithId(List<String> uIds, User currentUser);

     interface UserListResponse {
          void onSuccess(List<User> userList);
     }

     // Restaurants

     void getRestaurantList(List<NearBySearch.Results> requestNearBySearch, Go4LunchApi.RestaurantListResponse restaurantListResponse);

     void getRestaurantList(Go4LunchApi.RestaurantListResponse restaurantListResponse);

     Restaurant getRestaurantWithId(String idPlace, List<Restaurant> restaurants);

     interface RestaurantListResponse {
          void onSuccess(List<Restaurant> restaurantList);
     }

     // Participants

     void getParticipants(String idPlace, Go4LunchApi.ParticipantsResponse participantsResponse);

     interface ParticipantsResponse {
          void onSuccess(int participants);
     }
}


