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
import com.elytevolution.go4lunch.presenter.MapPresenter;
import com.elytevolution.go4lunch.view.activity.DetailsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class MapFragment extends Fragment implements MapPresenter.View, OnMapReadyCallback {

    private final LatLng location;

    private SupportMapFragment mapFragment;

    private MapPresenter presenter;

    public MapFragment(LatLng location) {
        this.location = location;
    }

    public static MapFragment newInstance(LatLng location) {
        return (new MapFragment(location));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new MapPresenter(this, getResources().getString(R.string.maps_api_key), location);
        presenter.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.support_map);

        if (mapFragment != null) updateMap();

        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if(permissionIsEnable()) {

            googleMap.setMyLocationEnabled(true);
            onMyLocationButtonClick(googleMap);
            onMyLocationClick(googleMap);
            presenter.addMarkerOnMap(googleMap);

            googleMap.setOnMarkerClickListener(marker -> {
                String string = (String) marker.getTag();
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("ID", string);
                this.startActivity(intent);
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

    public void onMyLocationButtonClick(GoogleMap googleMap){
        googleMap.setOnMyLocationButtonClickListener(() -> {
            showToastMessage(getString(R.string.my_location_map_fragment), Toast.LENGTH_SHORT);
            return false;
        });
    }

    public void onMyLocationClick(GoogleMap googleMap){
        googleMap.setOnMyLocationClickListener(location -> showToastMessage("Current location:\n" + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG));
    }

    @Override
    public void showToastMessage(String message, int duration) {
        Toast.makeText(getContext(), message, duration).show();
    }

    @Override
    public void updateMap() {
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}