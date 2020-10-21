package com.elytevolution.go4lunch.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.databinding.ActivityMainBinding;
import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.presenter.MainPresenter;
import com.elytevolution.go4lunch.view.adapter.ViewPagerFragmentAdapter;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    private MainPresenter presenter = new MainPresenter(this);

    private ActivityMainBinding binding;

    private ViewPager2 viewPager;

    private TabLayout tabLayout;

    private List<String> listNameViewPager = new ArrayList<>();

    private List<Integer> listImageViewPager = new ArrayList<>();

    private List<Restaurant> restaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        configureViewPagerAndTabLayout();
        startLocate();
    }


    private void configureViewPagerAndTabLayout() {
        viewPager = binding.mainActivityViewPager;
        tabLayout = binding.tabLayout;

        listNameViewPager.add("Map View");
        listNameViewPager.add("List View");
        listNameViewPager.add("Workmates");
        listImageViewPager.add(R.drawable.baseline_map_black_18dp);
        listImageViewPager.add(R.drawable.baseline_list_black_18dp);
        listImageViewPager.add(R.drawable.baseline_group_black_18dp);

        viewPager.setAdapter(new ViewPagerFragmentAdapter(restaurants, getSupportFragmentManager(), getLifecycle()));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setIcon(listImageViewPager.get(position))).attach();
        for (String list : listNameViewPager)
            tabLayout.getTabAt(listNameViewPager.indexOf(list)).setText(list);
    }

    private void startLocate() {
        // Initialize the SDK
        Places.initialize(getApplicationContext(), getString(R.string.google_api_key));

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.PHOTO_METADATAS, Place.Field.RATING);
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        restaurants.add(new Restaurant(
                                placeLikelihood.getPlace().getId(),
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getPlace().getAddress(),
                                placeLikelihood.getPlace().getLatLng(), null));
                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e("DEBUG", "Place not found: " + apiException.getStatusCode());
                    }
                }
            });
        }else{
            Log.d("PERMISSION", "PERMISSION NOT GRANTED");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startLocate();
    }
}
