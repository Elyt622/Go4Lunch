package com.elytevolution.go4lunch.view.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import java.util.ArrayList;
import java.util.List;

import static com.elytevolution.go4lunch.api.ParticipationHelper.createParticipation;
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
    public void onResponse(@Nullable NearBySearch tests) {
        if (tests != null) {
            updateUI(tests.getResults());
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
        String address, photoRef;
        Boolean currentOpen;
        Double rating;

        for(NearBySearch.Results result: results){
            currentOpen = result.getOpening_hours() == null ? null : result.getOpening_hours().getOpen_now();
            address = result.getVicinity();
            photoRef = result.getPhotos() == null ? null : result.getPhotos().get(0).getPhoto_reference();
            rating = result.getRating();
            restaurants.add(new Restaurant(result.getPlace_id(), result.getName(), address,
                    currentOpen, result.getGeometry().getLocation().getLng(),
                    result.getGeometry().getLocation().getLat(), 2, rating, photoRef));
        }
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
}
