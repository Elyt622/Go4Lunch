package com.elytevolution.go4lunch.presenter;

import android.util.Log;

import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.utilis.GooglePlaceCalls;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import static com.elytevolution.go4lunch.api.ParticipationHelper.createParticipation;
import static com.elytevolution.go4lunch.api.ParticipationHelper.getParticipationCollection;
import static com.elytevolution.go4lunch.api.ParticipationHelper.getParticipationDocument;

public class ListPresenter implements GooglePlaceCalls.Callbacks {

    private static final String TAG = "ListPresenter";

    private ListPresenter.View view;

    private int participation;

    private final List<Restaurant> restaurants;

    private final List<NearBySearch.Results> results = new ArrayList<>();

    public ListPresenter(View view, List<Restaurant> restaurants){
        this.view = view;
        this.restaurants = restaurants;
    }

    public void getAllRestaurant(List<NearBySearch.Results> results){
        String idPlace, name, address, photoRef;
        Boolean currentOpen;
        double rating, longitude, latitude;

        for(NearBySearch.Results result: results){
            idPlace = result.getPlace_id();
            name = result.getName();
            currentOpen = result.getOpening_hours() == null ? null : result.getOpening_hours().getOpen_now();
            address = result.getVicinity();
            photoRef = result.getPhotos() == null ? null : result.getPhotos().get(0).getPhoto_reference();
            rating = result.getRating();
            longitude = result.getGeometry().getLocation().getLng();
            latitude = result.getGeometry().getLocation().getLat();
            getParticipationToRestaurant(result.getPlace_id());

            restaurants.add(new Restaurant(idPlace, name, address, currentOpen, longitude,
                    latitude, participation, rating, photoRef));
        }
    }

    private void getParticipationToRestaurant(String idPlace){
        getParticipationDocument(idPlace).addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: " + snapshot.getData());
                List<String> list = (ArrayList<String>) snapshot.get("uid");
                participation = list == null ? 0 : list.size();
                getRestaurantWithId(idPlace).setParticipation(participation);
                view.updateAdapter();
            } else {
                Log.d(TAG, "Current data: null");
            }
        });
    }

    private Restaurant getRestaurantWithId(String idPlace){
        int index;
        for (index = 0; index < restaurants.size(); index++){
            if(restaurants.get(index).getIdPlace().equals(idPlace)){
                break;
            }
        }
        return restaurants.get(index);
    }

    public void insertParticipationInFireStore(){
        getAllRestaurant(results);
        for(Restaurant restaurant : restaurants) {
            getParticipationCollection().document(restaurant.getIdPlace()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (!task.getResult().exists()) {
                        createParticipation(restaurant.getIdPlace(), restaurant.getName(), null);
                    }
                }
            });
        }
    }

    public String convertLatLngToStringUrl(LatLng latLng){
        return (latLng.latitude + "," + latLng.longitude);
    }

    // Execute HTTP request and update UI
    public void executeHttpRequestWithRetrofit(String googleApiKey, LatLng location, String RADIUS, String TYPE){
        GooglePlaceCalls.fetchFollowing(this, convertLatLngToStringUrl(location), RADIUS, TYPE, googleApiKey);
    }

    @Override
    public void onResponse(@Nullable NearBySearch results) {
        if (results != null) {
            view.updateUI(results.getResults());
            insertParticipationInFireStore();
        } else {
            Log.d(TAG, "RESPONSE IS NULL");
        }
    }

    @Override
    public void onFailure() {
        Log.d(TAG, "FAILURE");
    }

    public void onDestroy(){
        this.view = null;
    }

    public interface View{
        void updateAdapter();
        void updateUI(List<NearBySearch.Results> results);
    }
}
