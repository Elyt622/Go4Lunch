package com.elytevolution.go4lunch.view.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.presenter.SignUpPresenter;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity implements SignUpPresenter.View{

    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword;

    private Button signUpButton;

    private SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        presenter = new SignUpPresenter(this, this);
        presenter.onCreate();

        signUpButton = findViewById(R.id.button_signup_signup_activity);
        editTextEmail = findViewById(R.id.editText_email_login_activity);
        editTextFirstName = findViewById(R.id.editText_first_name_login_activity);
        editTextLastName = findViewById(R.id.editText_last_name_login_activity);
        editTextPassword = findViewById(R.id.editText_password_login_activity);

        signUpButton.setOnClickListener(v -> presenter.createUserWithEmail(editTextEmail.getText().toString(),
                editTextPassword.getText().toString(), editTextFirstName.getText().toString(), editTextLastName.getText().toString()));
    }
}