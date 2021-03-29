package com.elytevolution.go4lunch.presenter;

import com.elytevolution.go4lunch.api.UserApi;
import com.elytevolution.go4lunch.api.UserLiveApi;
import com.elytevolution.go4lunch.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class WorkmatePresenter {

    private static final String TAG = "WorkmatePresenter";

    private WorkmatePresenter.View view;

    private final List<User> users = new ArrayList<>();

    private FirebaseUser currentUser;

    private final UserApi userApi;

    public WorkmatePresenter(WorkmatePresenter.View view, UserApi userApi){
        this.view = view;
        this.userApi = userApi;
    }

    public void getAllUsers(){
        clearListUser();
        userApi.getUserList(new UserLiveApi.UserListResponse() {
            @Override
            public void onSuccess(List<User> userList) {
                for(User user : userList) {
                    if (!user.getUid().equals(currentUser.getUid()))
                    users.add(user);
                }
                view.updateUI();
            }
        });
    }

    public String getUserId(int position) {
        return userApi.getUserId(position, users);
    }

    public String getUserName(int position) {
        return userApi.getUserName(position, users);
    }

    public String getUrlPicture(int position) {
        return userApi.getUrlPicture(position, users);
    }

    public String getPlaceId(int position) {
        return userApi.getPlaceId(position, users);
    }

    public int getUserListSize() {
        return userApi.getUserListSize(users);
    }

    public void clearListUser(){
        userApi.clearListUser(users);
    }

    public void initCurrentUser() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void onCreate(){
        initCurrentUser();
        getAllUsers();
    }

    public void onDestroy(){
        view = null;
    }

    public interface View {
        void updateUI();
    }
}
