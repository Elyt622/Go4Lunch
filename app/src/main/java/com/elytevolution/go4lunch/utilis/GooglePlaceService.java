package com.elytevolution.go4lunch.utilis;

import com.elytevolution.go4lunch.model.NearBySearch;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlaceService {
    @GET("json?")
    Call<NearBySearch> getResults(
            @Query("location") String latLng,
            @Query("radius") String radius,
            @Query("type") String type,
            @Query("key") String key);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
