package com.elytevolution.go4lunch.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.databinding.ActivityLoginBinding;
import com.elytevolution.go4lunch.presenter.LoginPresenter;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.View{

    private static final int GOOGLE_SIGN_IN = 123;

    private LoginButton facebookLoginButton;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        facebookLoginButton = binding.loginButton;
        SignInButton googleLoginButton = binding.signInButton;

        googleLoginButton.setSize(SignInButton.SIZE_STANDARD);

        presenter = new LoginPresenter(this, this);
        presenter.onCreate();

        googleLoginButton.setOnClickListener(v -> presenter.configureAuthGoogle(getString(R.string.default_web_client_id)));
        facebookLoginButton.setOnClickListener(v -> presenter.configureAuthFacebook(facebookLoginButton));
     }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.handleResponseAfterSignIn(resultCode, requestCode, data);
    }

    public void navigateToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void showToast(String message, int duration) {
        Toast.makeText(getApplicationContext(), message, duration).show();
    }

    @Override
    public void navigateToLoginGoogleScreen(Intent intent) {
        startActivityForResult(intent, GOOGLE_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}