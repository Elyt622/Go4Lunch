package com.elytevolution.go4lunch.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.presenter.MapPresenter;
import com.elytevolution.go4lunch.view.activity.DetailRestaurantActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class MapFragment extends Fragment implements MapPresenter.View, OnMapReadyCallback {

    private static final String RADIUS = "600";

    private static final String TYPE = "restaurant";

    private final LatLng location;

    private SupportMapFragment mapFragment;

    private MapPresenter presenter;

    private final List<NearBySearch.Results> results = new ArrayList<>();

    public MapFragment(LatLng location) {
        this.location = location;
    }

    public static MapFragment newInstance(LatLng location) {
        return (new MapFragment(location));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.support_map);

        presenter = new MapPresenter(this);
        presenter.executeHttpRequestWithRetrofit(getString(R.string.google_maps_key), location, RADIUS, TYPE);

        if (mapFragment != null) updateMap(results);


        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(permissionIsEnable()) {

            googleMap.setMyLocationEnabled(true);
            presenter.onMyLocationButtonClick(googleMap);
            presenter.onMyLocationClick(googleMap);
            presenter.addMarkerOnMap(googleMap);

            googleMap.setOnMarkerClickListener(marker -> {
                String string = (String) marker.getTag();
                Intent intent = new Intent(getContext(), DetailRestaurantActivity.class);
                intent.putExtra("ID", string);
                if(getContext() != null) getContext().startActivity(intent);
                return false;
            });
        }
    }

    public boolean permissionIsEnable() {
        if (getContext() != null){
            return (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMap(results);
    }

    @Override
    public void showToastMessage(String message, int duration) {
        Toast.makeText(getContext(), message, duration).show();
    }

    @Override
    public void updateMap(List<NearBySearch.Results> results) {
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}