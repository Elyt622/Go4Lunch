package com.elytevolution.go4lunch.api;

import com.elytevolution.go4lunch.model.Participation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ParticipationHelper {

    private static final String COLLECTION_NAME = "participation";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getParticipationCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createParticipation(String idPlace, List<String> uid) {
        Participation participationToCreate = new Participation(idPlace, uid);
        return ParticipationHelper.getParticipationCollection().document(idPlace).set(participationToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getParticipation(String idPlace){
        return ParticipationHelper.getParticipationCollection().document(idPlace).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateParticipation(String idPlace, List<String> uid) {
        return ParticipationHelper.getParticipationCollection().document(idPlace).update("uid", uid);
    }

    // --- DELETE ---

    public static Task<Void> deleteParticipation(String idPlace) {
        return ParticipationHelper.getParticipationCollection().document(idPlace).delete();
    }
}
