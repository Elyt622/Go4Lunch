package com.elytevolution.go4lunch.view.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elytevolution.go4lunch.MapViewInScroll;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.utilis.GooglePlaceCalls;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GooglePlaceCalls.Callbacks {

    MapViewInScroll mapViewInScroll;

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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

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
}