package com.elytevolution.go4lunch.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

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
import com.elytevolution.go4lunch.presenter.MainPresenter;
import com.elytevolution.go4lunch.view.adapter.ViewPagerFragmentAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    private MainPresenter presenter = new MainPresenter(this);

    private ActivityMainBinding binding;

    private ViewPager2 viewPager;

    private TabLayout tabLayout;

    private List<String> listNameViewPager = new ArrayList<>();

    private List<Integer> listImageViewPager = new ArrayList<>();

    private LocationManager locMan;

    private LatLng location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        configureViewPagerAndTabLayout();
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

        location = startLocate();

        viewPager.setAdapter(new ViewPagerFragmentAdapter(location, getSupportFragmentManager(), getLifecycle()));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setIcon(listImageViewPager.get(position))).attach();
        for (String list : listNameViewPager)
            tabLayout.getTabAt(listNameViewPager.indexOf(list)).setText(list);
    }

    private LatLng startLocate() {
        double lat = 0, lng = 0;
        locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        }
       else{
            Log.d("PERMISSION", "PERMISSION NOT GRANTED");
        }
       return new LatLng(lat,lng);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
