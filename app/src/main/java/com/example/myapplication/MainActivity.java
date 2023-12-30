package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button guest_button, signin_button, signup_button;
    TextView king;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        guest_button = findViewById(R.id.guest_button);
        signin_button = findViewById(R.id.sign_in_button);
        signup_button = findViewById(R.id.sign_up_button);
        king = findViewById(R.id.title_king);

        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}