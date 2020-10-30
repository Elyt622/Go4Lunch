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
import com.elytevolution.go4lunch.utilis.GooglePlaceCalls;
import com.elytevolution.go4lunch.view.adapter.RecyclerViewAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements GooglePlaceCalls.Callbacks{

    private RecyclerView recyclerView;

    private final List<NearBySearch.Results> tests = new ArrayList<>();

    private RecyclerViewAdapter adapter;

    private View view;

    private SwipeRefreshLayout swipeRefreshLayout;

    private LatLng location;

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
        adapter = new RecyclerViewAdapter(tests, location);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        configureSwipeRefreshLayout();
        return view;
    }

    @Override
    public void onResponse(@Nullable NearBySearch tests) {
        if (tests != null) {
            updateUI(tests.getResults());
        } else {
            Log.d("DEBUG", "RESPONSE IS NULL");
        }
    }

    @Override
    public void onFailure() {
        Log.d("DEBUG", "ECHEC");
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
        this.tests.clear();
        this.tests.addAll(results);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}