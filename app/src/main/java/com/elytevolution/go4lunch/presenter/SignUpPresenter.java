package com.elytevolution.go4lunch.presenter;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;
import static com.elytevolution.go4lunch.api.UserHelper.createUser;

public class SignUpPresenter {

    private final Activity activity;

    private SignUpPresenter.View view;

    private FirebaseAuth auth;

    private FirebaseUser currentUser;

    public SignUpPresenter (SignUpPresenter.View view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    public void createUserWithEmail(String email, String password, String firstname, String lastname) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        currentUser = auth.getCurrentUser();
                        createUser(auth.getUid(),firstname + ' ' + lastname, email, null, null);
                        activity.finish();
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(activity, "Sign up failed.",
                                Toast.LENGTH_SHORT).show();
                    }
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
