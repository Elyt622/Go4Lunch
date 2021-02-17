package com.elytevolution.go4lunch.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.databinding.ActivityMainBinding;
import com.elytevolution.go4lunch.presenter.MainPresenter;
import com.elytevolution.go4lunch.view.adapter.ViewPagerAdapter;
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
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import static com.elytevolution.go4lunch.api.UserHelper.getUser;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    private static final String TAG = "MainActivity";

    private static final int AUTOCOMPLETE_REQUEST_CODE = 123;

    private ActivityMainBinding binding;

    private DrawerLayout drawer;

    private ImageView imageUser;

    private TextView nameUser, emailUser;

    private FirebaseUser currentUser;

    private LatLng currentLocation = null;

    private Button activateLocalizationButton;

    private TextView textLocalization;

    private double latitude, longitude;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new MainPresenter(this);

        activateLocalizationButton = findViewById(R.id.button_activate_location_main_activity);
        textLocalization = findViewById(R.id.textview_activate_location_main_activity);

        startLocalization();

        if(!localizationIsEnable()){
            enableLocalization();
        }
        else {
            textLocalization.setVisibility(View.INVISIBLE);
            activateLocalizationButton.setVisibility(View.INVISIBLE);
            configureTabLayout();
        }

        configureNavigationView();
        drawer = findViewById(R.id.activity_main_drawer_layout);

        configureToolbar();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (!isLogged()) {
            finish();
        } else {
            getInfoUserOnMenuHeader();
        }
    }

    private void enableLocalization(){
            activateLocalizationButton.setOnClickListener(v -> startLocalization());
    }

    public void getInfoUserOnMenuHeader() {
        nameUser.setText(currentUser.getDisplayName());
        emailUser.setText(currentUser.getEmail());
        Glide.with(imageUser).load(currentUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(imageUser);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            case (android.R.id.home):
                drawer.openDrawer(GravityCompat.START);
                return true;
            case (R.id.action_search):
                configureAutocompleteSearch();
        }
        return true;
    }

    private boolean localizationIsEnable(){
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void configureAutocompleteSearch() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key), Locale.FRANCE);
        }

        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(latitude - 0.007, longitude - 0.007),
                new LatLng(latitude + 0.007, longitude + 0.007));

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.TYPES);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setLocationRestriction(bounds)
                .setCountries(Collections.singletonList("FR")).setTypeFilter(TypeFilter.ESTABLISHMENT)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && data != null) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if(place.getTypes().contains(Place.Type.RESTAURANT)) {
                    Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + place.getTypes());
                    Intent intent = new Intent(this, DetailsActivity.class);
                    intent.putExtra("ID", place.getId());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "It's not a restaurant", Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "The user canceled the operation");
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void configureNavigationView(){
        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
        View headerView = navigationView.getHeaderView(0);
        nameUser = headerView.findViewById(R.id.text_view_display_name_header_nav);
        emailUser = headerView.findViewById(R.id.text_view_email_header_nav);
        imageUser = headerView.findViewById(R.id.imageview_user_header_nav);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id){
                case (R.id.activity_main_drawer_lunch):
                    getUser(currentUser.getUid()).addOnSuccessListener(document -> {
                        String idPlace = document.getString("idPlace");
                        if(idPlace != null && !idPlace.equals("")) {
                            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                            intent.putExtra("ID", idPlace);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "You didn't select a restaurant yet!", Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                case (R.id.activity_main_drawer_settings):
                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    startActivity(intent);
                    break;
                case (R.id.activity_main_drawer_logout):
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    finish();
                    break;
                default:
                    break;
            }
            drawer.closeDrawer(GravityCompat.START);
            return false;
        });
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    private boolean isLogged() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private void configureTabLayout(){
        TabLayout tabLayout = binding.tabLayout;

        List<String> listNameViewPager = new ArrayList<>();
        List<Integer> listImageViewPager = new ArrayList<>();

        listNameViewPager.add("Map View");
        listNameViewPager.add("List View");
        listNameViewPager.add("Workmates");
        listImageViewPager.add(R.drawable.baseline_map_black_18dp);
        listImageViewPager.add(R.drawable.baseline_list_black_18dp);
        listImageViewPager.add(R.drawable.baseline_group_black_18dp);

        tabLayout.setupWithViewPager(configureViewPager());
        for (int icon : listImageViewPager) {
            tabLayout.getTabAt(listImageViewPager.indexOf(icon)).setIcon(icon);
        }
        for (String list : listNameViewPager)
            tabLayout.getTabAt(listNameViewPager.indexOf(list)).setText(list);
    }

    private ViewPager configureViewPager() {
        ViewPager viewPager = binding.mainActivityViewPager;
        Log.d(TAG, String.valueOf(currentLocation));
        ViewPagerAdapter adapter = new ViewPagerAdapter(currentLocation, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        return viewPager;
    }

    private void startLocalization() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        } else {
            Log.d(TAG, "PERMISSION NOT GRANTED");
        }
        currentLocation = new LatLng(latitude, longitude);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            startLocalization();
            configureTabLayout();
            textLocalization.setVisibility(View.INVISIBLE);
            activateLocalizationButton.setVisibility(View.INVISIBLE);
         }
    }
}
