package com.elytevolution.go4lunch.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.view.activity.DetailsActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import androidx.core.app.ActivityCompat;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.elytevolution.go4lunch.firestorerequest.UserHelper.getUser;

public class MainPresenter {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 123;

    private static final String TAG = "MainPresenter";

    private MainPresenter.View view;

    private LatLng currentLocation = null;

    private double latitude, longitude;

    private final String key;

    private FirebaseUser currentUser;

    private Activity activity;

    public MainPresenter(MainPresenter.View view, String key, Activity activity) {
        this.view = view;
        this.key = key;
        this.activity = activity;
    }

    private boolean localizationIsEnable(){
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void startLocalization() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (localizationIsEnable()) {
                @SuppressLint("MissingPermission") Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (lastKnownLocation != null) {
                    latitude = lastKnownLocation.getLatitude();
                }
                if (lastKnownLocation != null) {
                    longitude = lastKnownLocation.getLongitude();
                }
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        } else {
            Log.d(TAG, "PERMISSION NOT GRANTED");
        }
        currentLocation = new LatLng(latitude, longitude);
    }

    public void configureAutocompleteSearch() {
        if (!Places.isInitialized()) {
            Places.initialize(activity, key, Locale.FRANCE);
        }

        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(latitude - 0.007, longitude - 0.007),
                new LatLng(latitude + 0.007, longitude + 0.007));

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.TYPES);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setLocationRestriction(bounds)
                .setCountries(Collections.singletonList("FR")).setTypeFilter(TypeFilter.ESTABLISHMENT)
                .build(activity);
        activity.startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    public void configureActivity() {
        if (localizationIsEnable()) {
            view.setVisibilityLocalizationText(0);
            view.setVisibilityButtonActivateLocalization(0);
            view.configureTabLayout();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && data != null) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if(place.getTypes().contains(Place.Type.RESTAURANT)) {
                    Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + place.getTypes());
                    Intent intent = new Intent(activity, DetailsActivity.class);
                    intent.putExtra("ID", place.getId());
                    activity.startActivity(intent);
                }
                else{
                    Toast.makeText(activity, "It's not a restaurant", Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "The user canceled the operation");
            }
        }
    }

    public void initUser() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private boolean isLogged() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public void userIsLogged(){
        if (!isLogged()) {
            activity.finish();
        } else {
            getInfoUserOnMenuHeader();
        }
    }

    public void getInfoUserOnMenuHeader() {
        view.setUserName(currentUser.getDisplayName());
        view.setEmail(currentUser.getEmail());
        view.setProfilePicture(currentUser.getPhotoUrl());
    }

    public void setPermissions(int requestCode, int[] grantResults) {
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            startLocalization();
            view.configureTabLayout();
            view.setVisibilityButtonActivateLocalization(0);
            view.setVisibilityLocalizationText(0);
        }
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    // Action click

    public void clickOnMyLunch(){
        getUser(currentUser.getUid()).addOnSuccessListener(document -> {
            String idPlace = document.getString("idPlace");
            if(idPlace != null && !idPlace.equals("")) {
                Intent intent = new Intent(activity, DetailsActivity.class);
                intent.putExtra("ID", idPlace);
                activity.startActivity(intent);
            }
            else {
                Toast.makeText(activity, activity.getString(R.string.toast_no_choice), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void clickOnSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        activity.startActivity(intent);
    }

    public void clickOnLogout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        activity.finish();
    }

    // Activity Cycle

    public void onCreate() {
        initUser();
        userIsLogged();
        startLocalization();
        configureActivity();
    }

    public void onDestroy() {
        view = null;
        activity = null;
    }

    // View

    public interface View {
        void setProfilePicture(Uri urlPicture);
        void setUserName(String userName);
        void setEmail(String email);
        void setVisibilityLocalizationText(int visibility);
        void setVisibilityButtonActivateLocalization(int visibility);
        void configureTabLayout();
    }
}
