package com.elytevolution.go4lunch.presenter;

import android.util.Log;

import com.elytevolution.go4lunch.model.NearBySearch;
import com.elytevolution.go4lunch.utilis.GooglePlaceCalls;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import static com.elytevolution.go4lunch.firestorerequest.ParticipationHelper.getParticipation;

public class MapPresenter implements GooglePlaceCalls.Callbacks{

    private static final String TAG = "MapPresenter";

    private static final String RADIUS = "600";

    private static final String TYPE = "restaurant";

    private final String key;

    private final LatLng location;

    private MapPresenter.View view;

    private final List<NearBySearch.Results> results = new ArrayList<>();

    public MapPresenter(View view, String key, LatLng location){
        this.view = view;
        this.key = key;
        this.location = location;
    }

    public void addMarkerOnMap(GoogleMap map){

        for (NearBySearch.Results result : results) {

            String idPlace = result.getPlace_id();
            double latitude = result.getGeometry().getLocation().getLat();
            double longitude = result.getGeometry().getLocation().getLng();
            LatLng latLng = new LatLng(latitude, longitude);
            map.clear();
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
    public void onResponse(@Nullable NearBySearch result) {
        if(result != null) {
            results.addAll(result.getResults());
            view.updateMap();
        }
    }

    @Override
    public void onFailure() {
        Log.d(TAG, "Retrofit request failed");
    }

    public void executeHttpRequestWithRetrofit(String googleApiKey, LatLng location, String RADIUS, String TYPE){
        GooglePlaceCalls.fetchFollowing(this, convertLatLngToStringUrl(location), RADIUS, TYPE, googleApiKey);
    }

    public String convertLatLngToStringUrl(LatLng latLng){
        return (latLng.latitude + "," + latLng.longitude);
    }

    public void onCreate() { executeHttpRequestWithRetrofit(key, location, RADIUS, TYPE); }

    public void onStart() { view.updateMap(); }

    public void onDestroy(){
        view = null;
    }

    public interface View {
        void showToastMessage(String message, int duration);
        void updateMap();
    }
}
