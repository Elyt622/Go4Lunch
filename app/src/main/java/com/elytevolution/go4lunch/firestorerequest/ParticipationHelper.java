package com.elytevolution.go4lunch.firestorerequest;

import com.elytevolution.go4lunch.model.Participation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

    public static Task<Void> createParticipation(String idPlace, String namePlace, List<String> uid, String addressPlace) {
        Participation participationToCreate = new Participation(idPlace, namePlace, uid, addressPlace);
        return ParticipationHelper.getParticipationCollection().document(idPlace).set(participationToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getParticipation(String idPlace){
        return ParticipationHelper.getParticipationCollection().document(idPlace).get();
    }

    public static DocumentReference getParticipationDocument(String idPlace){
        return ParticipationHelper.getParticipationCollection().document(idPlace);
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
