package com.elytevolution.go4lunch.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.presenter.SplashPresenter;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity implements SplashPresenter.View {

    private SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        presenter = new SplashPresenter(this);
        presenter.onCreate(this);
    }

    @Override
    public void navigateToMainActivity(Intent intent) {
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}