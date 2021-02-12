package com.elytevolution.go4lunch.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.presenter.ListPresenter;
import com.elytevolution.go4lunch.view.adapter.RestaurantListAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ListFragment extends Fragment implements ListPresenter.View{

    private static final String RADIUS = "600";

    private static final String TYPE = "restaurant";

    private RestaurantListAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private final List<Restaurant> restaurants = new ArrayList<>();

    private final LatLng location;

    private ListPresenter presenter;

    public ListFragment(LatLng location) {
        this.location = location;
     }

    public static ListFragment newInstance(LatLng location) {
        return (new ListFragment(location));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_fragment_list);
        swipeRefreshLayout = view.findViewById(R.id.swipe_fragment_list);

        presenter = new ListPresenter(this, restaurants);
        presenter.executeHttpRequestWithRetrofit(getString(R.string.google_maps_key), location, RADIUS, TYPE);

        configureAdapter(view, recyclerView);
        configureSwipeRefreshLayout();

        return view;
    }

    private void configureAdapter(View view, RecyclerView recyclerView){
        adapter = new RestaurantListAdapter(restaurants, location, getString(R.string.google_maps_key));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void configureSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(() ->
                presenter.executeHttpRequestWithRetrofit(getString(R.string.google_maps_key), location, RADIUS, TYPE));
    }

    @Override
    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateUI(List<NearBySearch.Results> results) {
        restaurants.clear();
        presenter.getAllRestaurant(results);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
