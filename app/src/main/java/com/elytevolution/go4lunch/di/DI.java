package com.elytevolution.go4lunch.di;

import com.elytevolution.go4lunch.api.UserApi;
import com.elytevolution.go4lunch.api.UserLiveApi;

public class DI {
    UserApi service = new UserLiveApi();

    public UserApi getService() {
        return service;
    }

    public static UserApi getNewInstanceApiService() {
        return new UserLiveApi();
    }
}
