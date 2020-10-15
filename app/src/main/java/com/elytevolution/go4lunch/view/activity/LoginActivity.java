package com.elytevolution.go4lunch.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.elytevolution.go4lunch.databinding.ActivityLoginBinding;
import com.elytevolution.go4lunch.presenter.LoginPresenter;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.View {

    private ActivityLoginBinding binding;

    private static final int RC_SIGN_IN = 123;

    private ConstraintLayout constraintLayout;

    private LoginPresenter presenter = new LoginPresenter(this);

    private CallbackManager callbackManager;

    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        constraintLayout = binding.activityLoginConstraintLayout;
        loginButton = binding.loginButton;
     }

    // 2 - Show Snack Bar with a message
    public void showSnackBar(ConstraintLayout constraintLayout, String message){
        Snackbar.make(constraintLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 4 - Handle SignIn Activity response on activity result
        presenter.handleResponseAfterSignIn(constraintLayout, requestCode, resultCode, data);
    }
}