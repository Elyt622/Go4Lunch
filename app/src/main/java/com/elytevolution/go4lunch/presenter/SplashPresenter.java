package com.elytevolution.go4lunch.presenter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashPresenter {

    private SplashPresenter.View view;

    public SplashPresenter(SplashPresenter.View view) {
        this.view = view;
    }

    public void onDestroy() {
        view = null;
    }

    public FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public interface View {

    }
}
