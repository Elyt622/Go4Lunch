package com.elytevolution.go4lunch.api;

import android.util.Log;

import com.elytevolution.go4lunch.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.elytevolution.go4lunch.firestorerequest.UserHelper.getUsersCollection;

public class UserLiveApi implements UserApi {

    private static final String TAG = "USER_LIVE_API";

    public void getUserList(UserListResponse userListResponse) {
        List<User> users = new ArrayList<>();
        getUsersCollection().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                        users.add(new User(document.getString("uid"),
                                document.getString("displayName"),
                                document.getString("email"),
                                document.getString("urlPicture"),
                                document.getString("idPlace")));
                }
                userListResponse.onSuccess(users);
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    public List<String> removeCurrentUserWithId(List<String> uIds, FirebaseUser currentUser){
        List<String> listUser = new ArrayList<>();
        for(String uid: uIds){
            if(!uid.equals(currentUser.getUid())) {
                listUser.add(uid);
            }
        }
        return listUser;
    }

    @Override
    public List<String> removeCurrentUserWithId(List<String> uIds, User currentUser) {
        return null;
    }

}


