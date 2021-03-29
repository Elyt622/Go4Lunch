package com.elytevolution.go4lunch.fakeapi;

import com.elytevolution.go4lunch.api.UserApi;
import com.elytevolution.go4lunch.api.UserLiveApi;
import com.elytevolution.go4lunch.model.User;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class UserFakeApi implements UserApi {

    private List<User> users = new ArrayList<>();

    public void getUserList(UserLiveApi.UserListResponse userListResponse) {
        users = ModelGenerator.generateUsers();
        userListResponse.onSuccess(users);
    }

    public String getUserId(int position, List<User> users) {
        return users.get(position).getUid();
    }

    public String getUserName(int position, List<User> users) {
        return users.get(position).getDisplayName();
    }

    public String getUrlPicture(int position, List<User> users) {
        return users.get(position).getUrlPicture();
    }

    public String getPlaceId(int position, List<User> users) {
        return users.get(position).getIdPlace();
    }

    public int getUserListSize(List<User> users) {
        return users.size();
    }

    public void clearListUser(List<User> users){
        users.clear();
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
