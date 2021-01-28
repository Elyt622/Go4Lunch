package com.elytevolution.go4lunch.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.elytevolution.go4lunch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            if(currentUser != null) {
                intent = new Intent(getBaseContext(), MainActivity.class);
            }else {
                intent = new Intent(getBaseContext(), LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = auth.getCurrentUser();
    }
}