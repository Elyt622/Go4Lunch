package com.elytevolution.go4lunch.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elytevolution.go4lunch.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class DetailRestaurantActivity extends AppCompatActivity {

    private String idPlace;

    private Place place;

    private TextView textViewName, textViewAddress;

    private ImageView imageViewRestaurant, imageViewCall, imageViewLike, imageViewWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);

        if(getIntent() != null){
            idPlace = getIntent().getStringExtra("ID");
        }

        getDetailsPlaceLocation(idPlace);

        imageViewRestaurant = findViewById(R.id.image_view_restaurant_detail_activity);
        imageViewCall = findViewById(R.id.image_view_call_detail_activity);
        imageViewLike = findViewById(R.id.image_view_like_detail_activity);
        imageViewWebsite = findViewById(R.id.image_view_website_detail_activity);
        textViewName = findViewById(R.id.text_view_name_detail_activity);
        textViewAddress = findViewById(R.id.text_view_address_detail_activity);

        imageViewCall.setImageResource(R.drawable.ic_call_black_18dp);
        imageViewWebsite.setImageResource(R.drawable.ic_public_18px);
        imageViewLike.setImageResource(R.drawable.ic_star_rate_18px);

    }

    private void getDetailsPlaceLocation(String idPlace) {

        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyBAzeJeEsP2gNXjE_7XYMaywZECaJvmQAg");

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        // Specify the fields to return.
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS, Place.Field.WEBSITE_URI, Place.Field.PHONE_NUMBER, Place.Field.ADDRESS);

        // Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(idPlace, placeFields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                    place = response.getPlace();
                    textViewName.setText(place.getName());
                    textViewAddress.setText(place.getAddress());
                    if (place.getPhotoMetadatas() != null){
                        String imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                                + place.getPhotoMetadatas().get(0).zza()
                                + "&key=AIzaSyBAzeJeEsP2gNXjE_7XYMaywZECaJvmQAg";
                        Glide.with(this).load(imgUrl).into(imageViewRestaurant);
                    }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e("DEBUG", "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
            }
        });
    }
}