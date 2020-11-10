package com.elytevolution.go4lunch.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.model.User;
import com.elytevolution.go4lunch.view.adapter.DetailRestaurantListAdapter;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.elytevolution.go4lunch.api.ParticipationHelper.getParticipationCollection;
import static com.elytevolution.go4lunch.api.UserHelper.getUsersCollection;

public class DetailRestaurantActivity extends AppCompatActivity {

    private static String TAG = "DetailActivity";

    private String idPlace;

    private Place place;

    private TextView textViewName, textViewAddress;

    private RecyclerView recyclerView;

    private DetailRestaurantListAdapter adapter;

    private ImageView imageViewRestaurant, imageViewCall, imageViewLike, imageViewWebsite, imageViewFavorite;

    private List<String> usersIdParticipation = new ArrayList<>();

    private List<User> usersParticipants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);

        if(getIntent() != null){
            idPlace = getIntent().getStringExtra("ID");
        }

        getDetailsPlaceLocation(idPlace);
        getUsersIdParticipation(idPlace);
        imageViewRestaurant = findViewById(R.id.image_view_restaurant_detail_activity);
        imageViewCall = findViewById(R.id.image_view_call_detail_activity);
        imageViewLike = findViewById(R.id.image_view_like_detail_activity);
        imageViewWebsite = findViewById(R.id.image_view_website_detail_activity);
        imageViewFavorite = findViewById(R.id.image_view_favorite_detail_activity);

        textViewName = findViewById(R.id.text_view_name_detail_activity);
        textViewAddress = findViewById(R.id.text_view_address_detail_activity);

        recyclerView = findViewById(R.id.recycler_view_users_detail_activity);

        adapter = new DetailRestaurantListAdapter(usersParticipants);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageViewCall.setImageResource(R.drawable.ic_call_black_18dp);
        imageViewWebsite.setImageResource(R.drawable.ic_public_18px);
        imageViewLike.setImageResource(R.drawable.ic_star_rate_18px);

        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageViewFavorite.getVisibility() == View.VISIBLE) {
                    imageViewFavorite.setVisibility(View.INVISIBLE);
                }else{
                    imageViewFavorite.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getUsersIdParticipation(String idPlace){
        getParticipationCollection().whereEqualTo("idPlace", idPlace).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        usersIdParticipation = (ArrayList<String>) document.get("uid");
                    }
                    if (usersIdParticipation != null) {
                        getUserWithId(usersIdParticipation);
                    }
                }
            }
        });

    }

    private void getUserWithId(List<String> uIds){
        for(String user : uIds){
            getUsersCollection().whereEqualTo("uid", user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot user : task.getResult()){
                            usersParticipants.add(new User(user.getString("uid"),
                                    user.getString("firstName"),
                                    user.getString("lastName"),
                                    user.getString("email"),
                                    user.getString("urlPicture")));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
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
                        Glide.with(DetailRestaurantActivity.this).load(imgUrl).into(imageViewRestaurant);
                    }

            imageViewCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobileNumber = place.getPhoneNumber();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
                    intent.setData(Uri.parse("tel: " + mobileNumber)); // Data with intent respective action on intent
                    startActivity(intent);
                }
            });
            imageViewWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri url = place.getWebsiteUri();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(url);
                    startActivity(i);
                }
            });

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e("DEBUG", "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
            }
        });
    }
}