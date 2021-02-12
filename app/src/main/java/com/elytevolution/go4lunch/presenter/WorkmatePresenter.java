package com.elytevolution.go4lunch.presenter;

import android.util.Log;

import com.elytevolution.go4lunch.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

import static com.elytevolution.go4lunch.api.UserHelper.getUsersCollection;

public class WorkmatePresenter {

    private static final String TAG = "WorkmatePresenter";

    private WorkmatePresenter.View view;

    public WorkmatePresenter(WorkmatePresenter.View view){
        this.view = view;
    }

    public void getAllUsers(FirebaseUser currentUser, List<User> users){
        getUsersCollection()
                .get()
                .addOnCompleteListener(task -> {
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

    public void onDestroy(){
        view = null;
    }

    public interface View {
        void updateUI();
    }
}
