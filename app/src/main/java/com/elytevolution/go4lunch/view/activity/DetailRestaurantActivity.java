package com.elytevolution.go4lunch.view.activity;

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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.elytevolution.go4lunch.api.ParticipationHelper.getParticipation;
import static com.elytevolution.go4lunch.api.ParticipationHelper.updateParticipation;
import static com.elytevolution.go4lunch.api.UserHelper.getUser;
import static com.elytevolution.go4lunch.api.UserHelper.getUsersCollection;
import static com.elytevolution.go4lunch.api.UserHelper.updateUserIdPlace;

public class DetailRestaurantActivity extends AppCompatActivity {

    private static String TAG = "DetailActivity";

    private FirebaseUser currentUser;

    private boolean participate;

    private String idPlace;

    private Place place;

    private TextView textViewName, textViewAddress;

    private DetailRestaurantListAdapter adapter;

    private ImageView imageViewRestaurant, imageViewCall, imageViewLike, imageViewWebsite, imageViewFavorite, participateButton;

    private List<String> usersIdParticipation = new ArrayList<>();

    private List<User> usersParticipants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
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

        participateButton = findViewById(R.id.image_button_participation_detail_activity);

        textViewName = findViewById(R.id.text_view_name_detail_activity);
        textViewAddress = findViewById(R.id.text_view_address_detail_activity);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_users_detail_activity);

        adapter = new DetailRestaurantListAdapter(usersParticipants);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageViewCall.setImageResource(R.drawable.ic_call_black_18dp);
        imageViewWebsite.setImageResource(R.drawable.ic_public_18px);
        imageViewLike.setImageResource(R.drawable.ic_star_rate_18px);

        configButtonParticipation();

        participateButton.setOnClickListener(v -> {
            getParticipation(idPlace).addOnSuccessListener(documentSnapshot -> {
                List<String> list = (ArrayList) documentSnapshot.get("uid");
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (!participate) {
                    list.add(currentUser.getUid());
                    getUser(currentUser.getUid()).addOnSuccessListener(document -> {
                        String currentIdPlace = document.getString("idPlace");
                        if (currentIdPlace != null) {
                            if (!currentIdPlace.isEmpty()) {
                                getParticipation(currentIdPlace).addOnSuccessListener(document1 -> {
                                    List<String> list1 = (ArrayList) document1.get("uid");
                                    if (list1 == null) {
                                        list1 = new ArrayList<>();
                                    }
                                    list1 = removeCurrentUserWith(list1);
                                    updateParticipation(currentIdPlace, list1);
                                    updateUserIdPlace(idPlace, currentUser.getUid());
                                    configButtonParticipation();
                                });
                            } else{
                                updateUserIdPlace(idPlace, currentUser.getUid());
                                configButtonParticipation();
                            }
                        }
                    });
                } else {
                    list = removeCurrentUserWith(list);
                    updateUserIdPlace("", currentUser.getUid());
                }
                configButtonParticipation();
                updateParticipation(idPlace, list);
                usersParticipants.clear();
                addUserWithIds(list);
                adapter.notifyDataSetChanged();
            });
        });

        imageViewLike.setOnClickListener(v -> {
            if(imageViewFavorite.getVisibility() == View.VISIBLE) {
                imageViewFavorite.setVisibility(View.INVISIBLE);
            }else{
                imageViewFavorite.setVisibility(View.VISIBLE);
            }
        });
    }

    private void configButtonParticipation() {
        getUser(currentUser.getUid()).addOnSuccessListener(documentSnapshot -> {
            String idPlaceCurrentUser = documentSnapshot.getString("idPlace");
            if (idPlaceCurrentUser != null) {
                if (idPlaceCurrentUser.equals(idPlace)) {
                    participateButton.setImageResource(R.drawable.ic_check_circle_18px);
                    participate = true;
                } else {
                    participateButton.setImageResource(R.drawable.ic_check_circle_outline_black_18dp);
                    participate = false;
                }
            } else {
                participateButton.setImageResource(R.drawable.ic_check_circle_outline_black_18dp);
                participate = false;
            }
        });
    }

    private List<String> removeCurrentUserWith(List<String> uIds){
        List<String> listUser = new ArrayList<>();
        for(String uid: uIds){
            if(!uid.equals(currentUser.getUid())){
                listUser.add(uid);
            }
        }
        return listUser;
    }

    private void getUsersIdParticipation(String idPlace){
        getParticipation(idPlace).addOnSuccessListener(document -> {
            usersIdParticipation = (ArrayList<String>) document.get("uid");
            if (usersIdParticipation == null) {
                usersIdParticipation = new ArrayList<>();
            }
            addUserWithIds(usersIdParticipation);
        });
    }

    private void addUserWithIds(List<String> uIds){
        for(String user : uIds){
            getUsersCollection().whereEqualTo("uid", user).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot user1 : task.getResult()){
                        usersParticipants.add(new User(user1.getString("uid"),
                                user1.getString("displayName"),
                                user1.getString("email"),
                                user1.getString("urlPicture"), ""));
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void getDetailsPlaceLocation(String idPlace) {

        // Initialize the SDK
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        // Specify the fields to return.
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS, Place.Field.WEBSITE_URI, Place.Field.PHONE_NUMBER, Place.Field.ADDRESS);

        // Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(idPlace, placeFields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            place = response.getPlace();

            if(place.getName() != null) {
                if (place.getName().length() > 25) {
                    String phrase = place.getName().substring(0, 25) + "...";
                    textViewName.setText(phrase);
                } else {
                    textViewName.setText(place.getName());
                }
            }

            if(place.getAddress() != null) {
                if (place.getAddress().length() > 50) {
                    String phrase = place.getAddress().substring(0, 50) + "...";
                    textViewAddress.setText(phrase);
                } else {
                    textViewAddress.setText(place.getAddress());
                }
            }

            if (place.getPhotoMetadatas() != null && !place.getPhotoMetadatas().isEmpty()){
                String imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                        + place.getPhotoMetadatas().get(0).zza()
                        + "&key="+getString(R.string.google_maps_key);
                Glide.with(DetailRestaurantActivity.this).load(imgUrl).into(imageViewRestaurant);
            }

            imageViewCall.setOnClickListener(v -> {
                String mobileNumber = place.getPhoneNumber();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
                intent.setData(Uri.parse("tel: " + mobileNumber)); // Data with intent respective action on intent
                startActivity(intent);
            });
            imageViewWebsite.setOnClickListener(v -> {
                Uri url = place.getWebsiteUri();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(url);
                startActivity(i);
            });

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
            }
        });
    }
}