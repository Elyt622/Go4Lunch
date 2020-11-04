package com.elytevolution.go4lunch.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.elytevolution.go4lunch.RestaurantHelper.createRestaurant;
import static com.elytevolution.go4lunch.RestaurantHelper.getRestaurant;
import static com.elytevolution.go4lunch.RestaurantHelper.getRestaurantCollection;
import static com.elytevolution.go4lunch.UserHelper.createUser;

public class ListFragment extends Fragment implements GooglePlaceCalls.Callbacks{

    private RecyclerView recyclerView;

    private final List<NearBySearch.Results> results = new ArrayList<>();

    private RestaurantListAdapter adapter;

    private View view;

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
        view = inflater.inflate(R.layout.fragment_list, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_fragment_list);
        recyclerView = view.findViewById(R.id.recycler_view_fragment_list);

        adapter = new RestaurantListAdapter(results, location);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        configureSwipeRefreshLayout();
        return view;
    }

    @Override
    public void onResponse(@Nullable NearBySearch tests) {
        if (tests != null) {
            updateUI(tests.getResults());
            insertRestaurantInFireStore();
        } else {
            Log.d("DEBUG", "RESPONSE IS NULL");
        }
    }

    @Override
    public void onFailure() {
        Log.d("DEBUG", "FAILURE");
    }

    // ------------------------------
    //  HTTP REQUEST (Retrofit Way)
    // ------------------------------

    // Execute HTTP request and update UI
    private void executeHttpRequestWithRetrofit(){
        GooglePlaceCalls.fetchFollowing(this, convertLatLngToStringUrl(location), "5000", "restaurant", "AIzaSyBAzeJeEsP2gNXjE_7XYMaywZECaJvmQAg");
    }

    // 2 - Configure the SwipeRefreshLayout
    private void configureSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeHttpRequestWithRetrofit();
            }
        });
    }

    private String convertLatLngToStringUrl(LatLng latLng){
        return latLng.latitude+","+latLng.longitude;
    }

    private void updateUI(List<NearBySearch.Results> results) {
        this.results.clear();
        this.results.addAll(results);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void allRestaurant(List<NearBySearch.Results> results){
        for(NearBySearch.Results result: results){
            restaurants.add(new Restaurant(result.getPlace_id()));
        }
    }

    private void insertRestaurantInFireStore(){
        allRestaurant(results);
        for(Restaurant restaurant : restaurants)
            getRestaurantCollection().document(restaurant.getIdPlace()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {
                            createRestaurant(restaurant.getIdPlace());
                        }
                    }
                }
            });
    }
}
