package com.elytevolution.go4lunch.view.activity;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.databinding.ActivityMainBinding;
import com.elytevolution.go4lunch.view.adapter.ViewPagerFragmentAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import static com.elytevolution.go4lunch.api.UserHelper.createUser;
import static com.elytevolution.go4lunch.api.UserHelper.getUser;
import static com.elytevolution.go4lunch.api.UserHelper.getUsersCollection;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    private List<String> listNameViewPager = new ArrayList<>();

    private List<Integer> listImageViewPager = new ArrayList<>();

    private Toolbar toolbar;

    private NavigationView navigationView;

    private DrawerLayout drawer;

    private ImageView imageUser;

    private TextView nameUser, emailUser;

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        configureNavigationView();
        drawer = findViewById(R.id.activity_main_drawer_layout);

        configureViewPagerAndTabLayout();
        configureToolbar();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (!isLogged()) {
            finish();
        } else {
            getInfoUserOnMenuHeader();
            if (currentUser != null) {
                getUsersCollection().document(currentUser.getUid()).get().addOnCompleteListener(task -> {
                    if (!task.getResult().exists()) {
                        createUser(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getEmail(), String.valueOf(currentUser.getPhotoUrl()), "");
                    }
                });
            }
        }
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
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return true;
    }

    private void configureNavigationView(){
        navigationView = findViewById(R.id.activity_main_nav_view);
        View headerView = navigationView.getHeaderView(0);
        nameUser = headerView.findViewById(R.id.text_view_display_name_header_nav);
        emailUser = headerView.findViewById(R.id.text_view_email_header_nav);
        imageUser = headerView.findViewById(R.id.imageview_user_header_nav);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void configureToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.activity_main_drawer_lunch:
                getUser(currentUser.getUid()).addOnSuccessListener(document -> {
                        String idPlace = document.getString("idPlace");
                        Intent intent = new Intent(this, DetailRestaurantActivity.class);
                        intent.putExtra("ID", idPlace);
                        startActivity(intent);
                });
                break;
            case R.id.activity_main_drawer_settings:
                Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;
            case R.id.activity_main_drawer_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isLogged() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private void configureViewPagerAndTabLayout() {
        ViewPager viewPager = binding.mainActivityViewPager;
        TabLayout tabLayout = binding.tabLayout;

        listNameViewPager.add("Map View");
        listNameViewPager.add("List View");
        listNameViewPager.add("Workmates");
        listImageViewPager.add(R.drawable.baseline_map_black_18dp);
        listImageViewPager.add(R.drawable.baseline_list_black_18dp);
        listImageViewPager.add(R.drawable.baseline_group_black_18dp);

        LatLng location = startLocate();

        viewPager.setAdapter(new ViewPagerFragmentAdapter(location, getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        for (int icon : listImageViewPager) {
            tabLayout.getTabAt(listImageViewPager.indexOf(icon)).setIcon(icon);
        }
        for (String list : listNameViewPager)
            tabLayout.getTabAt(listNameViewPager.indexOf(list)).setText(list);
    }

    private LatLng startLocate() {
        double lat = 0, lng = 0;
        LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locMan != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (lastLoc != null) {
                    lat = lastLoc.getLatitude();
                }
                if (lastLoc != null) {
                    lng = lastLoc.getLongitude();
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        } else {
            Log.d(TAG, "PERMISSION NOT GRANTED");
        }
        return new LatLng(lat, lng);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            configureViewPagerAndTabLayout();
        }
    }
}
