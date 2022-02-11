package com.elytevolution.go4lunch.presenter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.elytevolution.go4lunch.view.activity.LoginActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import static com.elytevolution.go4lunch.firestorerequest.UserHelper.createUser;
import static com.elytevolution.go4lunch.firestorerequest.UserHelper.getUsersCollection;
import static com.elytevolution.go4lunch.firestorerequest.UserHelper.updateUserFcmToken;

import java.util.Arrays;

public class LoginPresenter {

    public static final int GOOGLE_SIGN_IN = 123;

    private static final String TAG = "LoginPresenter";

    private LoginPresenter.View view;

    private FirebaseAuth auth;

    private FirebaseUser currentUser;

    private GoogleSignInClient googleSignInClient;

    private CallbackManager callbackManager;

    private Activity activity;

    public LoginPresenter(LoginPresenter.View view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    public void handleResponseAfterSignIn(int resultCode, int requestCode, Intent data){
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
        else if(requestCode == FacebookSdk.getCallbackRequestCodeOffset()){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void signInWithEmailAndPassword(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        currentUser = auth.getCurrentUser();
                        if (currentUser != null) view.navigateToMainActivity();
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        currentUser = auth.getCurrentUser();
                        getFCMToken();
                        if (currentUser != null) view.navigateToMainActivity();
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }

    public void firebaseAuthWithFacebook(String idToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(idToken);
        auth.signInWithCredential(credential).addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        currentUser = auth.getCurrentUser();
                        getFCMToken();
                        if (currentUser != null) view.navigateToMainActivity();
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        view.showToast( "This email already exists with an other account", Toast.LENGTH_LONG);
                        LoginManager.getInstance().logOut();
                    }
                });
    }

    public void configureAuthFacebook(ImageButton facebookLoginButton) {
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("email", "public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            firebaseAuthWithFacebook(loginResult.getAccessToken().getToken());
                        }

                        @Override
                        public void onCancel() {
                            Log.d(TAG, "Callback result: Canceled");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Log.d(TAG, "Callback result: Error");
                        }
                    });
        });
    }

    public void configureAuthGoogle(String idWebClient){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(idWebClient)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(activity, gso);
        signInWithGoogle();
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        view.navigateToLoginGoogleScreen(signInIntent);
    }

    public void createNewUser(String fcmToken) {
        if(currentUser != null && currentUser.getEmail() != null) {
            getUsersCollection().document(currentUser.getEmail()).get().addOnCompleteListener(task -> {
                if (!task.getResult().exists()) {
                    createUser(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getEmail(), String.valueOf(currentUser.getPhotoUrl()), "", fcmToken);
                }
                else{
                    updateUserFcmToken(fcmToken, currentUser.getUid());
                }
            });
        }
    }

    public void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> createNewUser(task.getResult()));
    }

    public void initFirebaseAuth() {
        auth = FirebaseAuth.getInstance();
    }

    public void onCreate() {
        initFirebaseAuth();
    }

    public void onDestroy(){
        view = null;
        activity = null;
    }

    public interface View {
        void showToast(String message, int duration);
        void navigateToLoginGoogleScreen(Intent intent);
        void navigateToMainActivity();
    }
}
