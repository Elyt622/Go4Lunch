package com.elytevolution.go4lunch.view.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elytevolution.go4lunch.customview.MapViewInScroll;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.utilis.GooglePlaceCalls;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GooglePlaceCalls.Callbacks, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private MapViewInScroll mapViewInScroll;

    private GoogleMap googleMap;

    private final List<NearBySearch.Results> tests = new ArrayList<>();

    private LatLng location;

    public MapFragment(LatLng location) {
        this.location = location;
    }

    public static MapFragment newInstance(LatLng location) {
        return (new MapFragment(location));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        executeHttpRequestWithRetrofit();
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mapViewInScroll = view.findViewById(R.id.mapView);
        mapViewInScroll.onCreate(savedInstanceState);
        mapViewInScroll.onResume();
        mapViewInScroll.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener(this);
            map.setOnMyLocationClickListener(this);
        }
    }


    // Execute HTTP request and update UI
    private void executeHttpRequestWithRetrofit(){
        GooglePlaceCalls.fetchFollowing(this, convertLatLngToStringUrl(location), "5000", "restaurant", "AIzaSyBAzeJeEsP2gNXjE_7XYMaywZECaJvmQAg");
    }

    private String convertLatLngToStringUrl(LatLng latLng){
        return latLng.latitude+","+latLng.longitude;
    }

    private void updateUI(List<NearBySearch.Results> results) {
        this.tests.clear();
        this.tests.addAll(results);
        addMarkerOnMap();
    }

    private void addMarkerOnMap(){
        for (NearBySearch.Results result : tests) {
            Log.d("MapsFragment","test");
            double lat = result.getGeometry().getLocation().getLat();
            double lgt = result.getGeometry().getLocation().getLng();
            LatLng latLng = new LatLng(lat, lgt);
            googleMap.addMarker(new MarkerOptions().position(latLng).title(result.getName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public void onResponse(@Nullable NearBySearch results) {
        if(results != null) {
            Log.d("MapsFragment","test");
            tests.addAll(results.getResults());
            updateUI(tests);
        }
    }

    @Override
    public void onFailure() {
        Log.d("Maps Fragment", "Retrofit request failed");
    }

    @Override
    public void onResume() {
        super.onResume();
        mapViewInScroll.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapViewInScroll.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapViewInScroll.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapViewInScroll.onLowMemory();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "My location", Toast.LENGTH_SHORT)
                .show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG)
                .show();
    }
}