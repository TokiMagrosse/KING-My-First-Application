package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    Button register_button;
    TextView back_to_login;

    private EditText username, email_address, register_password, confirm_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_button = findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });

        back_to_login = findViewById(R.id.back_to_login_activity);
        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        username = findViewById(R.id.username);
        email_address = findViewById(R.id.email_address);
        register_password = findViewById(R.id.register_password);
        confirm_password = findViewById(R.id.confirm_password);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void checkCredentials() {
        String checkUsername = username.getText().toString();
        String checkEmailAddress = email_address.getText().toString();
        String checkPassword = register_password.getText().toString();
        String checkConfirmedPassword = confirm_password.getText().toString();

        if (checkUsername.isEmpty()) {
            showError(username, "Please enter your username");
        }
        else if (checkUsername.length() <= 5) {
            showError(username, "Your username length must be at least 6 characters");
        }
        else if (checkEmailAddress.isEmpty()) {
            showError(email_address, "Please enter your email");
        }
        else if (!checkEmailAddress.contains("@")) {
            showError(email_address, "Please enter a valid email address");
        }
        else if (checkPassword.isEmpty()) {
            showError(register_password, "Please enter your password");
        }
        else if (checkPassword.length() < 8) {
            showError(register_password, "Your password length must be at least 8 characters");
        }
        else if (checkPassword.length() > 64) {
            showError(register_password, "Your password can have at most 64 characters");
        }
        else if (checkConfirmedPassword.isEmpty() || !checkConfirmedPassword.equals(checkPassword)) {
            showError(confirm_password, "Your password doesn't match the previous one");
        }
        else {
            Toast.makeText(this, "YOU HAVE SUCCESSFULLY REGISTERED!", Toast.LENGTH_LONG).show();
        }
    }

    private void showError(EditText input, String errorText) {
        input.setError(errorText);
        input.requestFocus();
    }
}