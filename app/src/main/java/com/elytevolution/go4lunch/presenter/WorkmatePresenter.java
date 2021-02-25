package com.elytevolution.go4lunch.presenter;

import android.util.Log;

import com.elytevolution.go4lunch.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.elytevolution.go4lunch.api.UserHelper.getUsersCollection;

public class WorkmatePresenter {

    private static final String TAG = "WorkmatePresenter";

    private WorkmatePresenter.View view;

    private final List<User> users = new ArrayList<>();

    private FirebaseUser currentUser;

    public WorkmatePresenter(WorkmatePresenter.View view){
        this.view = view;
    }

    public void getAllUsers(){
        clearListUser();
        getUsersCollection().get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(!document.getId().equals(currentUser.getUid()))
                                users.add(new User(document.getString("uid"),
                                        document.getString("displayName"),
                                        document.getString("email"),
                                        document.getString("urlPicture"),
                                        document.getString("idPlace")));
                        }
                        view.updateUI();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
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

    public void clearListUser(){
        users.clear();
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
