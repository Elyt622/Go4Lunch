package com.elytevolution.go4lunch.fakeapi;

import com.elytevolution.go4lunch.api.UserApi;
import com.elytevolution.go4lunch.api.UserLiveApi;
import com.elytevolution.go4lunch.model.User;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class UserFakeApi implements UserApi {

    public void getUserList(UserLiveApi.UserListResponse userListResponse) {
        List<User> users = ModelGenerator.generateUsers();
        userListResponse.onSuccess(users);
    }

    public List<String> removeCurrentUserWithId(List<String> uIds, User currentUser){
        List<String> listUser = new ArrayList<>();
        for(String uid: uIds){
            if(!uid.equals(currentUser.getUid())){
                listUser.add(uid);
            }
        }
        return listUser;
    }

    @Override
    public List<String> removeCurrentUserWithId(List<String> uIds, FirebaseUser currentUser) {
        return null;
    }

}
