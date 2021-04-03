package com.elytevolution.go4lunch.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.elytevolution.go4lunch.firestorerequest.ParticipationHelper.getParticipationDocument;

public class ParticipantsLiveApi implements ParticipantsApi{

    private static final String TAG = "";

    public void getParticipants(String idPlace, ParticipantsApi.ParticipantsResponse participantsResponse){
        getParticipationDocument(idPlace).addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: " + snapshot.getData());
                List<String> list = (ArrayList<String>) snapshot.get("uid");
                int participants = list == null ? 0 : list.size();
                participantsResponse.onSuccess(participants);
            } else {
                Log.d(TAG, "Current data: null");
            }
        });
    }
}
