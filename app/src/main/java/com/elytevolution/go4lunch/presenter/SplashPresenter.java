package com.elytevolution.go4lunch.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.elytevolution.go4lunch.view.activity.LoginActivity;
import com.elytevolution.go4lunch.view.activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashPresenter {

    private SplashPresenter.View view;

    private FirebaseUser currentUser;

    public SplashPresenter(SplashPresenter.View view) {
        this.view = view;
    }

    public void configureSplashScreen(Activity activity){
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            if(currentUser != null) {
                intent = new Intent(activity, MainActivity.class);
            }else {
                intent = new Intent(activity, LoginActivity.class);
            }
            view.navigateToMainActivity(intent);
        }, 2000);
    }

    public void initCurrentUser(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void onStart(Activity activity){
        initCurrentUser();
        configureSplashScreen(activity);
    }

    public void onCreate(Activity activity){
        initCurrentUser();
        configureSplashScreen(activity);
    }

    public void onDestroy() {
        view = null;
    }

    public interface View {
        void navigateToMainActivity(Intent intent);
    }
}
