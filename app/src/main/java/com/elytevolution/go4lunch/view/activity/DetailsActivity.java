package com.elytevolution.go4lunch.view.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.di.DI;
import com.elytevolution.go4lunch.presenter.DetailsPresenter;
import com.elytevolution.go4lunch.view.adapter.DetailsAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DetailsActivity extends AppCompatActivity implements DetailsPresenter.View {

    private DetailsPresenter presenter;

    private DetailsAdapter adapter;

    private TextView textViewName, textViewAddress;

    private ImageView imageViewFavorite, participateButton, imageViewRestaurant;

    private RecyclerView recyclerView;

    private String idPlace;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);

        if(getIntent() != null){
            idPlace = getIntent().getStringExtra("ID");
        }

        presenter = new DetailsPresenter(this, idPlace, getString(R.string.google_api_key), this, DI.getNewInstanceApiService());
        presenter.onStart();

        imageViewRestaurant = findViewById(R.id.image_view_restaurant_detail_activity);
        ImageView imageViewCall = findViewById(R.id.image_view_call_detail_activity);
        ImageView imageViewLike = findViewById(R.id.image_view_like_detail_activity);
        ImageView imageViewWebsite = findViewById(R.id.image_view_website_detail_activity);
        imageViewFavorite = findViewById(R.id.image_view_favorite_detail_activity);

        participateButton = findViewById(R.id.image_button_participation_detail_activity);

        textViewName = findViewById(R.id.text_view_name_detail_activity);
        textViewAddress = findViewById(R.id.text_view_address_detail_activity);

        recyclerView = findViewById(R.id.recycler_view_users_detail_activity);

        toolbar = findViewById(R.id.toolbar_details_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureRecyclerView();

        imageViewCall.setImageResource(R.drawable.ic_call_black_18dp);
        imageViewWebsite.setImageResource(R.drawable.ic_public_18px);
        imageViewLike.setImageResource(R.drawable.ic_star_rate_18px);

        presenter.configButtonParticipation();
        participateButton.setOnClickListener(v -> presenter.updatePart());

        presenter.configButtonFavorite();
        imageViewLike.setOnClickListener(v -> presenter.updateFavoriteList());

        imageViewCall.setOnClickListener(v -> startActivity(presenter.navigateToCallIntent()));

        imageViewWebsite.setOnClickListener(v -> startActivity(presenter.navigateToWebsiteIntent()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void configureRecyclerView(){
        adapter = new DetailsAdapter(presenter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public void showButtonFavorite() {
        imageViewFavorite.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void hideButtonFavorite() {
        imageViewFavorite.setVisibility(android.view.View.INVISIBLE);
    }

    @Override
    public void activateButtonParticipation() {
        participateButton.setImageResource(R.drawable.ic_check_circle_18px);
    }

    @Override
    public void deactivateButtonParticipation() {
        participateButton.setImageResource(R.drawable.ic_check_circle_outline_black_18dp);
    }

    @Override
    public void setAddressText(String address) {
        textViewAddress.setText(address);
    }

    @Override
    public void setPhotoRestaurant(String imgUrl) {
        Glide.with(DetailsActivity.this).load(imgUrl).into(imageViewRestaurant);
    }

    @Override
    public void setNameText(String name) {
        textViewName.setText(name);
    }

    @Override
    public void updateRecyclerView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}