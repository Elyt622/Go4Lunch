package com.elytevolution.go4lunch.presenter;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.ContentValues.TAG;
import static com.elytevolution.go4lunch.firestorerequest.UserHelper.createUser;

public class SignUpPresenter {

    private final Activity activity;

    private SignUpPresenter.View view;

    private FirebaseAuth auth;

    public SignUpPresenter (SignUpPresenter.View view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    public void createUserWithEmail(String email, String password, String firstname, String lastname) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        getFCMToken(email, firstname, lastname);
                        activity.finish();
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(activity, "Sign up failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getFCMToken(String email, String firstname, String lastname) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            createUser(auth.getUid(),firstname + ' ' + lastname, email, null, null, task.getResult());
        });
    }

    public void onCreate() {
        initFirebaseAuth();
    }

    private void initFirebaseAuth() {
        auth = FirebaseAuth.getInstance();
    }

    public interface View {

    }
}
