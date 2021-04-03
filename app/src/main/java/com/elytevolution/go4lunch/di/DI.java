package com.elytevolution.go4lunch.di;

import com.elytevolution.go4lunch.api.Go4LunchApi;
import com.elytevolution.go4lunch.api.Go4LunchLiveApi;

public class DI {
    Go4LunchApi service = new Go4LunchLiveApi();

    public Go4LunchApi getService() {
        return service;
    }

    public static Go4LunchApi getNewInstanceApiService() {
        return new Go4LunchLiveApi();
    }
}
