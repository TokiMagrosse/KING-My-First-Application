package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView register_text_reference, forgot_password;
    Button login_button;
    CheckBox remember_me;
    EditText email_address, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register_text_reference = findViewById(R.id.register_text_reference);
        forgot_password = findViewById(R.id.forgot_password);
        login_button = findViewById(R.id.login_button);
        remember_me = findViewById(R.id.remember_me);
        email_address = findViewById(R.id.email_address);
        password = findViewById(R.id.password);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        register_text_reference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

            /*private void startActivity(Intent intent) {

            }*/
        });
    }

}