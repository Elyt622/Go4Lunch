package com.elytevolution.go4lunch.api;

import com.elytevolution.go4lunch.model.User;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public interface UserApi {

     //WorkmatePresenter function

     void getUserList(UserLiveApi.UserListResponse userListResponse);

     //DetailsPresenter function

     List<String> removeCurrentUserWithId(List<String> uIds, FirebaseUser currentUser);

     List<String> removeCurrentUserWithId(List<String> uIds, User currentUser);

     interface UserListResponse {
          void onSuccess(List<User> userList);
     }
}


