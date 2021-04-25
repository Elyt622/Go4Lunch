package com.elytevolution.go4lunch.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.databinding.ActivityMainBinding;
import com.elytevolution.go4lunch.presenter.MainPresenter;
import com.elytevolution.go4lunch.view.adapter.ViewPagerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    private DrawerLayout drawer;

    private ImageView imageUser;

    private TextView nameUser, emailUser, textLocalization;

    private Button activateLocalizationButton;

    private MainPresenter presenter;

    private ViewPager viewPager;

    private TabLayout tabLayout;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new MainPresenter(this, getString(R.string.google_maps_key), this);

        configureNavigationView();

        activateLocalizationButton = findViewById(R.id.button_activate_location_main_activity);
        textLocalization = findViewById(R.id.textview_activate_location_main_activity);
        viewPager = binding.mainActivityViewPager;
        tabLayout = binding.tabLayout;
        drawer = findViewById(R.id.activity_main_drawer_layout);
        navigationView = findViewById(R.id.activity_main_nav_view);

        presenter.onCreate();

        activateLocalizationButton.setOnClickListener(v -> presenter.startLocalization());

        configureToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            case (android.R.id.home):
                drawer.openDrawer(GravityCompat.START);
                return true;
            case (R.id.action_search):
                presenter.configureAutocompleteSearch();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void configureNavigationView(){
        navigationView = findViewById(R.id.activity_main_nav_view);
        View headerView = navigationView.getHeaderView(0);
        nameUser = headerView.findViewById(R.id.text_view_display_name_header_nav);
        emailUser = headerView.findViewById(R.id.text_view_email_header_nav);
        imageUser = headerView.findViewById(R.id.imageview_user_header_nav);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id){
                case (R.id.activity_main_drawer_lunch):
                    presenter.clickOnMyLunch();
                    break;
                case (R.id.activity_main_drawer_settings):
                    presenter.clickOnSettings();
                    break;
                case (R.id.activity_main_drawer_logout):
                    presenter.clickOnLogout();
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

    public ViewPager configureViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(presenter.getCurrentLocation(), getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        return viewPager;
    }

    public void configureTabLayout(){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.setPermissions(requestCode, grantResults);
    }

    @Override
    public void setProfilePicture(Uri urlPicture) {
        Glide.with(imageUser).load(urlPicture).apply(RequestOptions.circleCropTransform()).into(imageUser);
    }

    @Override
    public void setUserName(String userName) {
        nameUser.setText(userName);
    }

    @Override
    public void setEmail(String email) {
        emailUser.setText(email);

    }

    @Override
    public void setVisibilityLocalizationText(int visibility) {
        if(visibility == 0){
            textLocalization.setVisibility(View.INVISIBLE);
        }else {
            textLocalization.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setVisibilityButtonActivateLocalization(int visibility) {
        if(visibility == 0){
            activateLocalizationButton.setVisibility(View.INVISIBLE);
        }else {
            activateLocalizationButton.setVisibility(View.VISIBLE);
        }
    }
}
