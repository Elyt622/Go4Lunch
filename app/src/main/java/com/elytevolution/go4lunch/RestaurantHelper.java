package com.elytevolution.go4lunch;

import com.elytevolution.go4lunch.model.Restaurant;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RestaurantHelper {

    private static final String COLLECTION_NAME = "restaurant";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getRestaurantCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createRestaurant(String uid) {
        Restaurant restaurantToCreate = new Restaurant(uid);
        return RestaurantHelper.getRestaurantCollection().document(uid).set(restaurantToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getRestaurant(String uid){
        return RestaurantHelper.getRestaurantCollection().document(uid).get();
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return RestaurantHelper.getRestaurantCollection().document(uid).delete();
    }
}
