package com.elytevolution.go4lunch.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.elytevolution.go4lunch.api.UserApi;
import com.elytevolution.go4lunch.model.User;
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

import static com.elytevolution.go4lunch.api.FavoriteHelper.createFavorite;
import static com.elytevolution.go4lunch.api.FavoriteHelper.getFavorite;
import static com.elytevolution.go4lunch.api.FavoriteHelper.updateFavorite;
import static com.elytevolution.go4lunch.api.ParticipationHelper.getParticipation;
import static com.elytevolution.go4lunch.api.ParticipationHelper.updateParticipation;
import static com.elytevolution.go4lunch.api.UserHelper.getUser;
import static com.elytevolution.go4lunch.api.UserHelper.getUsersCollection;
import static com.elytevolution.go4lunch.api.UserHelper.updateUserIdPlace;

public class DetailsPresenter {

    private static final String TAG = "DetailsPresenter";

    private FirebaseUser currentUser;

    private boolean participate, favorite;

    private Place place;

    private final String idPlace;

    private final String key;

    private List<String> usersIdParticipation = new ArrayList<>();

    private List<String> favoriteList = new ArrayList<>();

    private final List<User> usersParticipants = new ArrayList<>();

    private DetailsPresenter.View view;

    private Activity activity;

    private final UserApi userApi;

    public DetailsPresenter(DetailsPresenter.View view, String idPlace, String key, Activity activity, UserApi userApi){
        this.view = view;
        this.idPlace = idPlace;
        this.key = key;
        this.activity = activity;
        this.userApi = userApi;
    }

    public void updatePart(){
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
                                list1 = userApi.removeCurrentUserWithId(list1, currentUser);
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
                list = userApi.removeCurrentUserWithId(list, currentUser);
                updateUserIdPlace("", currentUser.getUid());
            }
            configButtonParticipation();
            updateParticipation(idPlace, list);
            usersParticipants.clear();
            addUserWithIds(list);
            view.updateRecyclerView();
        });
    }

    public void configButtonFavorite(){
        getFavorite(currentUser.getUid()).addOnSuccessListener(documentSnapshot -> {
            if(!documentSnapshot.exists()){
                createFavorite(null, currentUser.getUid());
            }
            else {
                favoriteList = (ArrayList) documentSnapshot.get("idPlace");
                if (favoriteList == null)
                    favoriteList = new ArrayList<>();
                if (favoriteList.contains(idPlace)) {
                    favorite = true;
                    view.showButtonFavorite();
                } else {
                    favorite = false;
                    view.hideButtonFavorite();
                }
            }
        });
    }

    public void configButtonParticipation() {
        getUser(currentUser.getUid()).addOnSuccessListener(documentSnapshot -> {
            String idPlaceCurrentUser = documentSnapshot.getString("idPlace");
            if (idPlaceCurrentUser != null) {
                if (idPlaceCurrentUser.equals(idPlace)) {
                    view.activateButtonParticipation();
                    participate = true;
                } else {
                    view.deactivateButtonParticipation();
                    participate = false;
                }
            } else {
                view.deactivateButtonParticipation();
                participate = false;
            }
        });
    }

    /*public List<String> removeCurrentUserWithId(List<String> uIds){
        List<String> listUser = new ArrayList<>();
        for(String uid: uIds){
            if(!uid.equals(currentUser.getUid())){
                listUser.add(uid);
            }
        }
        return listUser;
    }*/

    private void getUsersIdParticipation(){
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
                    view.updateRecyclerView();
                }
            });
        }
    }

    private void getDetailsPlaceLocation() {
        Places.initialize(activity, key);

        PlacesClient placesClient = Places.createClient(activity);

        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS, Place.Field.WEBSITE_URI, Place.Field.PHONE_NUMBER, Place.Field.ADDRESS);

        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(idPlace, placeFields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            place = response.getPlace();

            Log.d(TAG, formatString(place.getName(),25));
            view.setNameText(formatString(place.getName(), 25));

            view.setAddressText(formatString(place.getAddress(), 50));

            if (place.getPhotoMetadatas() != null && !place.getPhotoMetadatas().isEmpty()){
                String imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                        + place.getPhotoMetadatas().get(0).zza()
                        + "&key=" + key;
                view.setPhotoRestaurant(imgUrl);
            }

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
            }
        });
    }

    private String formatString(String stringToCut, int numberMax){
        String stringResult = null;
        if(stringToCut != null) {
            if (stringToCut.length() > numberMax) {
                stringResult = stringToCut.substring(0, numberMax) + "...";
            } else {
                stringResult = stringToCut;
            }
        }
        return stringResult;
    }

    public void updateFavoriteList(){
        configButtonFavorite();
        if(favorite) {
            favoriteList.remove(idPlace);
        }else {
            favoriteList.add(idPlace);
        }
        updateFavorite(favoriteList, currentUser.getUid());
        configButtonFavorite();

    }

    public Intent navigateToCallIntent(){
        String mobileNumber = place.getPhoneNumber();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel: " + mobileNumber));
        return intent;
    }

    public Intent navigateToWebsiteIntent(){
        Uri url = place.getWebsiteUri();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(url);
        return intent;
    }

    public void initCurrentUser(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void onStart() {
        initCurrentUser();
        getDetailsPlaceLocation();
        getUsersIdParticipation();
    }

    public void onDestroy() {
        view = null;
        activity = null;
    }

    public String showUserIsJoining(int position, String joiningToPrint) {
        return getUserName(position) + " " + joiningToPrint;
    }

    public String getUserName(int position) {
        return usersParticipants.get(position).getDisplayName();
    }

    public String getUrlPicture(int position) {
        return usersParticipants.get(position).getUrlPicture();
    }

    public int getListParticipantSize(){
        return usersParticipants.size();
    }

    public interface View{
        void showButtonFavorite();
        void hideButtonFavorite();
        void activateButtonParticipation();
        void deactivateButtonParticipation();
        void setNameText(String name);
        void setAddressText(String address);
        void setPhotoRestaurant(String imgUrl);
        void updateRecyclerView();
    }
}
