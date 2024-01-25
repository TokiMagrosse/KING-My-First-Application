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
import android.widget.Toast;

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
        register_text_reference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

        });

        forgot_password = findViewById(R.id.forgot_password);
        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });

        remember_me = findViewById(R.id.remember_me);
        email_address = findViewById(R.id.email_address_or_username);
        password = findViewById(R.id.password);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


    }

    private void checkCredentials() {
        String checkEmailAddress = email_address.getText().toString();
        String checkPassword = password.getText().toString();


        if (checkEmailAddress.isEmpty()) {
            showError(email_address, "Please enter your email");
        }
        else if (!checkEmailAddress.contains("@")) {
            showError(email_address, "Please enter a valid email address");
        }
        else if (checkPassword.isEmpty()) {
            showError(password, "Please enter your password");
        }
        else if (checkPassword.length() < 8) {
            showError(password, "Your password length must be at least 8 characters");
        }
        else if (checkPassword.length() > 64) {
            showError(password, "Your password can have at most 64 characters");
        }
        else {
            Toast.makeText(this, "YOU HAVE SUCCESSFULLY LOGGED IN!", Toast.LENGTH_LONG).show();
        }
    }

    private void showError(EditText input, String errorText) {
        input.setError(errorText);
        input.requestFocus();
    }


}

