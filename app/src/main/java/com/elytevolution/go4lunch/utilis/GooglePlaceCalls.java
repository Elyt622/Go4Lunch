package com.elytevolution.go4lunch.utilis;

import android.util.Log;

import com.elytevolution.go4lunch.model.NearBySearch;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GooglePlaceCalls {
    private static final String TAG = "GooglePlace" ;

    // 1 - Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable NearBySearch results);
        void onFailure();
    }

    public static void fetchFollowing(Callbacks callbacks, String latLng, String radius, String type, String key){

        // 2.1 - Create a weak reference to callback (avoid memory leaks)
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

        // 2.2 - Get a Retrofit instance and the related endpoints
        GooglePlaceService googlePlaceService = GooglePlaceService.retrofit.create(GooglePlaceService.class);

        // 2.3 - Create the call on Google API
        Call<NearBySearch> call = googlePlaceService.getResults(latLng, radius, type, key);

        // 2.4 - Start the call
        call.enqueue(new Callback<NearBySearch>() {

            @Override
            public void onResponse(@NonNull Call<NearBySearch> call, @NonNull Response<NearBySearch> response) {
                // 2.5 - Call the proper callback used in controller (ListFragment)
                Log.d(TAG, String.valueOf(call.request()));
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<NearBySearch> call, @NonNull Throwable t) {
                // 2.5 - Call the proper callback used in controller (ListFragment)
                Log.d(TAG, String.valueOf(t.getMessage()));
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}
