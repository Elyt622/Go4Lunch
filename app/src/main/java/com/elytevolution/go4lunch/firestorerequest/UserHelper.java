package com.elytevolution.go4lunch.firestorerequest;

import com.elytevolution.go4lunch.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.Nullable;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String displayName, String email, String urlPicture, @Nullable String idPlace, String fcmToken) {
        User userToCreate = new User(uid, displayName, email, urlPicture, idPlace, fcmToken);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUserIdPlace(String idPlace, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("idPlace", idPlace);
    }

    public static Task<Void> updateUserFcmToken(String fcmToken, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("fcmToken", fcmToken);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }
}
