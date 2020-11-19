package com.elytevolution.go4lunch.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.utilis.GooglePlaceCalls;
import com.elytevolution.go4lunch.view.activity.DetailRestaurantActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static com.elytevolution.go4lunch.api.ParticipationHelper.getParticipation;

public class MapFragment extends Fragment implements OnMapReadyCallback, GooglePlaceCalls.Callbacks, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private static final String TAG = "MapFragment";

    private static final String RADIUS = "600";

    private static final String TYPE = "restaurant";

    private final List<NearBySearch.Results> results = new ArrayList<>();

    private LatLng location;

    private SupportMapFragment mapFragment;

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
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.support_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener(this);
            map.setOnMyLocationClickListener(this);
            addMarkerOnMap(map);
            map.setOnMarkerClickListener(marker -> {
                String string = (String) marker.getTag();
                Intent intent = new Intent(getContext(), DetailRestaurantActivity.class);
                intent.putExtra("ID", string);
                getContext().startActivity(intent);
                return false;
            });
        }
    }


    // Execute HTTP request and update UI
    private void executeHttpRequestWithRetrofit(){
        GooglePlaceCalls.fetchFollowing(this, convertLatLngToStringUrl(location), RADIUS, TYPE, getString(R.string.google_maps_key));
    }

    private String convertLatLngToStringUrl(LatLng latLng){
        return latLng.latitude+","+latLng.longitude;
    }

    private void addMarkerOnMap(GoogleMap map){
        for (NearBySearch.Results result : results) {
            String idPlace = result.getPlace_id();
            double lat = result.getGeometry().getLocation().getLat();
            double lgt = result.getGeometry().getLocation().getLng();
            LatLng latLng = new LatLng(lat, lgt);
            getParticipation(idPlace).addOnSuccessListener(documentSnapshot -> {
                List<String> participants = (ArrayList) documentSnapshot.get("uid");
                if (participants != null && participants.size() != 0) {
                    Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(result.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    marker.setTag(idPlace);
                }else {
                    Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(result.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    marker.setTag(idPlace);
                }
            });
        }
    }

    @Override
    public void onResponse(@Nullable NearBySearch results) {
        if(results != null) {
            this.results.addAll(results.getResults());
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onFailure() {
        Log.d(TAG, "Retrofit request failed");
    }


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "My location", Toast.LENGTH_SHORT)
                .show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location:\n" + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapFragment.getMapAsync(this);
    }
}