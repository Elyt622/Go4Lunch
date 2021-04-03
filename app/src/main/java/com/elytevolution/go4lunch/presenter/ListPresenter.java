package com.elytevolution.go4lunch.presenter;

import android.util.Log;

import com.elytevolution.go4lunch.api.ParticipantsApi;
import com.elytevolution.go4lunch.api.ParticipantsLiveApi;
import com.elytevolution.go4lunch.api.RestaurantApi;
import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.utilis.GooglePlaceCalls;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import static com.elytevolution.go4lunch.firestorerequest.ParticipationHelper.createParticipation;
import static com.elytevolution.go4lunch.firestorerequest.ParticipationHelper.getParticipationCollection;

public class ListPresenter implements GooglePlaceCalls.Callbacks {

    private static final String TAG = "ListPresenter";

    private static final String RADIUS = "600";

    private static final String TYPE = "restaurant";

    private ListPresenter.View view;

    private List<Restaurant> restaurants = new ArrayList<>();

    private final List<NearBySearch.Results> requestNearBySearch = new ArrayList<>();

    private final String key;

    private final LatLng currentLocation;

    private final RestaurantApi restaurantApi;

    public void setListRestaurant(List<Restaurant> restaurants){
        this.restaurants = restaurants;
    }

    public ListPresenter(View view, LatLng currentLocation, String key, RestaurantApi restaurantApi){
        this.view = view;
        this.currentLocation = currentLocation;
        this.key = key;
        this.restaurantApi = restaurantApi;
    }

    public void getAllRestaurant(){
        restaurants.clear();
        restaurantApi.getRestaurantList(requestNearBySearch, new RestaurantApi.RestaurantListResponse() {
            @Override
            public void onSuccess(List<Restaurant> restaurantList) {
                restaurants = restaurantList;
                getAllParticipants();
                view.notifyDataSetChanged();
                view.setRefreshing(false);
            }
        });
    }

    public void getAllParticipants(){
        ParticipantsApi participantsApi = new ParticipantsLiveApi();
        for (Restaurant restaurant : restaurants) {
            participantsApi.getParticipants(restaurant.getIdPlace(), new ParticipantsApi.ParticipantsResponse() {
                @Override
                public void onSuccess(int participants) {
                    restaurant.setParticipation(participants);
                    view.notifyDataSetChanged();
                }
            });
        }
    }

    public void insertParticipationInFireStore(){
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

    public void executeHttpRequestWithRetrofit(String googleApiKey, LatLng location, String RADIUS, String TYPE){
        GooglePlaceCalls.fetchFollowing(this, convertLatLngToStringUrl(location), RADIUS, TYPE, googleApiKey);
    }

    @Override
    public void onResponse(@Nullable NearBySearch results) {
        requestNearBySearch.clear();
        if (results != null) {
            requestNearBySearch.addAll(results.getResults());
            getAllRestaurant();
            insertParticipationInFireStore();
        } else {
            Log.d(TAG, "RESPONSE IS NULL");
        }
    }

    @Override
    public void onFailure() {
        Log.d(TAG, "FAILURE");
    }

    public String getName(int position) {
        return restaurants.get(position).getName();
    }

    public String getAddress(int position) {
        return restaurants.get(position).getAddress();
    }

    public int getSizeRestaurant() {
        return restaurants.size();
    }

    public String getPhotoReference(int position) {
        return restaurants.get(position).getPhotoRef();
    }

    public Boolean getOpenRestaurant(int position) {
        return restaurants.get(position).isCurrentOpen();
    }

    public int getParticipants(int position) {
        return restaurants.get(position).getParticipation();
    }

    public Double getRating(int position) {
        return restaurants.get(position).getRating();
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public LatLng getRestaurantLocation(int position) {
        return new LatLng(restaurants.get(position).getLat(), restaurants.get(position).getLgt());
    }

    public String getRestaurantId(int position) {
        return restaurants.get(position).getIdPlace();
    }

    public String getGoogleKey() {
        return key;
    }

    public double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sinLat = Math.sin(dLat / 2);
        double sinLng = Math.sin(dLng / 2);
        double a = Math.pow(sinLat, 2) + Math.pow(sinLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
    }

    public void onCreate(){
        executeHttpRequestWithRetrofit(key, currentLocation, RADIUS, TYPE);
    }

    public void onDestroy(){
        this.view = null;
    }

    public void onStart() {
        view.notifyDataSetChanged();
    }

    public interface View{
        void notifyDataSetChanged();
        void setRefreshing(boolean refreshing);
    }
}
