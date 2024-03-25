package com.example.poisonousking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class StartingSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_splash);

        new Handler().postDelayed(() -> {
            // Start the main activity after the delay
            Intent intent = new Intent(StartingSplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }, 3500); // 3.5 seconds delay, adjust as needed
    }
}
