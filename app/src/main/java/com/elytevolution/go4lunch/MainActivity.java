package com.elytevolution.go4lunch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}