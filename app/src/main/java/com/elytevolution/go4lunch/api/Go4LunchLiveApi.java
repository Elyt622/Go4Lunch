package com.elytevolution.go4lunch.api;

import android.util.Log;

import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.model.User;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.elytevolution.go4lunch.firestorerequest.ParticipationHelper.getParticipationDocument;
import static com.elytevolution.go4lunch.firestorerequest.UserHelper.getUsersCollection;

public class Go4LunchLiveApi implements Go4LunchApi {

    private static final String TAG = "USER_LIVE_API";

    public void getUserList(Go4LunchApi.UserListResponse userListResponse) {
        List<User> users = new ArrayList<>();
        getUsersCollection().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                        users.add(new User(document.getString("uid"),
                                document.getString("displayName"),
                                document.getString("email"),
                                document.getString("urlPicture"),
                                document.getString("idPlace")));
                }
                userListResponse.onSuccess(users);
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    public List<String> removeCurrentUserWithId(List<String> uIds, String currentUserId){
        List<String> listUser = new ArrayList<>();
        for(String uid: uIds){
            if(!uid.equals(currentUserId)) {
                listUser.add(uid);
            }
        }
        return listUser;
    }

    public void getParticipants(String idPlace, Go4LunchApi.ParticipantsResponse participantsResponse){
        getParticipationDocument(idPlace).addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: " + snapshot.getData());
                List<String> list = (ArrayList<String>) snapshot.get("uid");
                int participants = list == null ? 0 : list.size();
                participantsResponse.onSuccess(participants);
            } else {
                Log.d(TAG, "Current data: null");
            }
        });
    }

    public void getRestaurantList(List<NearBySearch.Results> requestNearBySearch, Go4LunchApi.RestaurantListResponse restaurantListResponse) {

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

}


