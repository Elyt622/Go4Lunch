package com.elytevolution.go4lunch.firestorerequest;

import com.elytevolution.go4lunch.model.Favorite;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FavoriteHelper {

    private static final String COLLECTION_NAME = "favorite";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getFavoriteCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createFavorite(List<String> idPlace, String uid) {
        Favorite favoriteToCreate = new Favorite(idPlace, uid);
        return FavoriteHelper.getFavoriteCollection().document(uid).set(favoriteToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getFavorite(String uid){
        return FavoriteHelper.getFavoriteCollection().document(uid).get();
    }

    public static DocumentReference getFavoriteDocument(String uid){
        return FavoriteHelper.getFavoriteCollection().document(uid);
    }

    // --- UPDATE ---

    public static Task<Void> updateFavorite(List<String> idPlace, String uid) {
        return FavoriteHelper.getFavoriteCollection().document(uid).update("idPlace", idPlace);
    }

    // --- DELETE ---

    public static Task<Void> deleteParticipation(String uid) {
        return FavoriteHelper.getFavoriteCollection().document(uid).delete();
    }
}
