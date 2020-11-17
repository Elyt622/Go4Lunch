package com.elytevolution.go4lunch.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.utilis.GooglePlaceCalls;
import com.elytevolution.go4lunch.view.adapter.RestaurantListAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.elytevolution.go4lunch.api.ParticipationHelper.createParticipation;
import static com.elytevolution.go4lunch.api.ParticipationHelper.getParticipation;
import static com.elytevolution.go4lunch.api.ParticipationHelper.getParticipationCollection;

public class ListFragment extends Fragment implements GooglePlaceCalls.Callbacks{

    private static final String TAG = "ListFragment";

    private static final String RADIUS = "600";

    private static final String TYPE = "restaurant";

    private final List<NearBySearch.Results> results = new ArrayList<>();

    private RestaurantListAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private LatLng location;

    private List<Restaurant> restaurants = new ArrayList<>();

    private int participation;

    public ListFragment(LatLng location) {
        this.location = location;
     }

    public static ListFragment newInstance(LatLng location) {
        return (new ListFragment(location));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        executeHttpRequestWithRetrofit();
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_fragment_list);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_fragment_list);

        adapter = new RestaurantListAdapter(restaurants, location, getString(R.string.google_maps_key));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        configureSwipeRefreshLayout();
        return view;
    }

    @Override
    public void onResponse(@Nullable NearBySearch results) {
        if (results != null) {
            updateUI(results.getResults());
            insertParticipationInFireStore();
        } else {
            Log.d(TAG, "RESPONSE IS NULL");
        }
    }

    @Override
    public void onFailure() {
        Log.d(TAG, "FAILURE");
    }

    // ------------------------------
    //  HTTP REQUEST (Retrofit Way)
    // ------------------------------

    // Execute HTTP request and update UI
    private void executeHttpRequestWithRetrofit(){
        GooglePlaceCalls.fetchFollowing(this, convertLatLngToStringUrl(location), RADIUS, TYPE, getString(R.string.google_maps_key));
    }

    // 2 - Configure the SwipeRefreshLayout
    private void configureSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(this::executeHttpRequestWithRetrofit);
    }

    private String convertLatLngToStringUrl(LatLng latLng){
        return latLng.latitude+","+latLng.longitude;
    }

    private void updateUI(List<NearBySearch.Results> results) {
        restaurants.clear();
        listAllRestaurant(results);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void listAllRestaurant(List<NearBySearch.Results> results){
        String idPlace, name, address, photoRef;
        Boolean currentOpen;
        double rating, lgt, lat;

        for(NearBySearch.Results result: results){
            idPlace = result.getPlace_id();
            name = result.getName();
            currentOpen = result.getOpening_hours() == null ? null : result.getOpening_hours().getOpen_now();
            address = result.getVicinity();
            photoRef = result.getPhotos() == null ? null : result.getPhotos().get(0).getPhoto_reference();
            rating = result.getRating();
            lgt = result.getGeometry().getLocation().getLng();
            lat = result.getGeometry().getLocation().getLat();
            participationToRestaurant(result.getPlace_id());

            restaurants.add(new Restaurant(idPlace, name, address, currentOpen, lgt,
                    lat, participation, rating, photoRef));
        }
    }

    private void participationToRestaurant(String idPlace){
        getParticipation(idPlace).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                List<String> list = (ArrayList<String>) document.get("uid");
                participation = list == null ? 0 : list.size();
                getRestaurantWithId(idPlace).setParticipation(participation);
                adapter.notifyDataSetChanged();
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

    private void insertParticipationInFireStore(){
        listAllRestaurant(results);
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

    @Override
    public void onStart() {
        super.onStart();
        executeHttpRequestWithRetrofit();
    }
}
