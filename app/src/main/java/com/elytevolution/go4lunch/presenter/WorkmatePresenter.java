package com.elytevolution.go4lunch.presenter;

import com.elytevolution.go4lunch.api.Go4LunchApi;
import com.elytevolution.go4lunch.api.Go4LunchLiveApi;
import com.elytevolution.go4lunch.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class WorkmatePresenter {

    private static final String TAG = "WorkmatePresenter";

    private WorkmatePresenter.View view;

    private List<User> users = new ArrayList<>();

    private FirebaseUser currentUser;

    private final Go4LunchApi go4LunchApi;

    public WorkmatePresenter(WorkmatePresenter.View view, Go4LunchApi go4LunchApi){
        this.view = view;
        this.go4LunchApi = go4LunchApi;
    }

    public void getAllUsers(String uid){
        users.clear();
        go4LunchApi.getUserList(new Go4LunchLiveApi.UserListResponse() {
            @Override
            public void onSuccess(List<User> userList) {
                for(User user : userList) {
                    if (!user.getUid().equals(uid)) users.add(user);
                }
                if (view != null) view.updateUI();
            }
        });
    }

    public String getUserId(int position) {
        return users.get(position).getUid();
    }

    public String getUserName(int position) {
        return users.get(position).getDisplayName();
    }

    public String getUrlPicture(int position) {
        return users.get(position).getUrlPicture();
    }

    public String getPlaceId(int position) {
        return users.get(position).getIdPlace();
    }

    public int getUserListSize() {
        return users.size();
    }

    public void initCurrentUser() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getCurrentUserId() {
        return currentUser.getUid();
    }

    public void onCreate(){
        initCurrentUser();
        getAllUsers(getCurrentUserId());
    }

    public void onDestroy(){
        view = null;
    }

    public interface View {
        void updateUI();
    }
}
