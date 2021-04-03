package com.elytevolution.go4lunch.api;

public interface ParticipantsApi {

    void getParticipants(String idPlace, ParticipantsApi.ParticipantsResponse participantsResponse);

    interface ParticipantsResponse {
        void onSuccess(int participants);
    }
}
