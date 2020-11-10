package com.elytevolution.go4lunch.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.databinding.ActivityMainBinding;
import com.elytevolution.go4lunch.view.adapter.ViewPagerFragmentAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.elytevolution.go4lunch.api.UserHelper.createUser;
import static com.elytevolution.go4lunch.api.UserHelper.getUsersCollection;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    private List<String> listNameViewPager = new ArrayList<>();

    private List<Integer> listImageViewPager = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        configureViewPagerAndTabLayout();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(!isLogged()){
            finish();
        }else {
            if (currentUser != null) {
                getUsersCollection().document(currentUser.getUid()).get().addOnCompleteListener(task -> {
                    if(!task.getResult().exists()){
                        createUser(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getDisplayName(), currentUser.getEmail(), String.valueOf(currentUser.getPhotoUrl()));
                    }
                });
            }
        }
        }

    private boolean isLogged(){
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
        for(int icon : listImageViewPager) {
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
                    lat  = lastLoc.getLatitude();
                }
                if (lastLoc != null) {
                    lng = lastLoc.getLongitude();
                }
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        }
       else{
            Log.d(TAG, "PERMISSION NOT GRANTED");
        }
       return new LatLng(lat,lng);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            configureViewPagerAndTabLayout();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
