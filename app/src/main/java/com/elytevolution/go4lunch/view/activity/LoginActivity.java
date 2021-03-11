package com.elytevolution.go4lunch.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.databinding.ActivityLoginBinding;
import com.elytevolution.go4lunch.presenter.LoginPresenter;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import androidx.appcompat.app.AppCompatActivity;

import static com.elytevolution.go4lunch.presenter.LoginPresenter.GOOGLE_SIGN_IN;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.View{

    private LoginButton facebookLoginButton;

    private EditText editTextEmail, editTextPassword;

    private LoginPresenter presenter;

    private TextView textViewSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        TwitterLoginButton twitterLoginButton = binding.twitterLoginButton;
        facebookLoginButton = binding.loginButton;
        SignInButton googleLoginButton = binding.signInButton;
        Button loginButton = binding.buttonLoginLoginActivity;
        editTextEmail = binding.editTextEmailLoginActivity;
        editTextPassword = binding.editTextPasswordLoginActivity;
        textViewSignUp = binding.textViewSignupLoginActivity;

        googleLoginButton.setStyle(SignInButton.SIZE_WIDE, SignInButton.COLOR_AUTO);

        presenter = new LoginPresenter(this, this, twitterLoginButton);
        presenter.onCreate();

        googleLoginButton.setOnClickListener(v -> presenter.configureAuthGoogle(getString(R.string.default_web_client_id)));
        facebookLoginButton.setOnClickListener(v -> presenter.configureAuthFacebook(facebookLoginButton));
        loginButton.setOnClickListener(v -> presenter.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString()));
        textViewSignUp.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));
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
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}