package com.elytevolution.go4lunch.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.databinding.ActivityLoginBinding;
import com.elytevolution.go4lunch.presenter.LoginPresenter;

import androidx.appcompat.app.AppCompatActivity;

import static com.elytevolution.go4lunch.presenter.LoginPresenter.GOOGLE_SIGN_IN;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.View{

    private ImageButton facebookLoginButton;

    private EditText editTextEmail, editTextPassword;

    private LoginPresenter presenter;

    private TextView textViewSignUp;

    private ImageButton googleLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        facebookLoginButton = binding.loginButton;
        googleLoginButton = binding.signInButton;
        Button loginButton = binding.buttonLoginLoginActivity;
        editTextEmail = binding.editTextEmailLoginActivity;
        editTextPassword = binding.editTextPasswordLoginActivity;
        textViewSignUp = binding.textViewSignupLoginActivity;

        presenter = new LoginPresenter(this, this);
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