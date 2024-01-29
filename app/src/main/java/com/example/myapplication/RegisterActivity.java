package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText username, email_address, register_password, confirm_password;
    Button register_button;
    TextView back_to_login;
    FirebaseAuth m_auth;
    ProgressBar progress_bar;

    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = m_auth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            // finish();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email_address = findViewById(R.id.email_address);
        register_password = findViewById(R.id.register_password);
        confirm_password = findViewById(R.id.confirm_password);
        m_auth = FirebaseAuth.getInstance();
        progress_bar = findViewById(R.id.progress_bar);
        register_button = findViewById(R.id.register_button);
        back_to_login = findViewById(R.id.back_to_login_activity);
        register_button.setOnClickListener(v -> checkCredentials());

        if (m_auth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
        }

        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkCredentials() {
        String checkUsername = username.getText().toString().trim();
        String checkEmailAddress = email_address.getText().toString().trim();
        String checkPassword = register_password.getText().toString().trim();
        String checkConfirmedPassword = confirm_password.getText().toString().trim();
        boolean isValid = true;

        if (checkUsername.isEmpty()) {
            showError(username, "Please enter your username");
            isValid = false;
        }
        else if (checkUsername.length() <= 5) {
            showError(username, "Your username length must be at least 6 characters");
            isValid = false;
        }
        else if (checkEmailAddress.isEmpty()) {
            showError(email_address, "Please enter your email");
            isValid = false;
        }
        else if (!checkEmailAddress.contains("@")) {
            showError(email_address, "Please enter a valid email address");
            isValid = false;
        }
        else if (checkPassword.isEmpty()) {
            showError(register_password, "Please enter your password");
            isValid = false;
        }
        else if (checkPassword.length() < 8) {
            showError(register_password, "Your password length must be at least 8 characters");
            isValid = false;
        }
        else if (checkPassword.length() > 64) {
            showError(register_password, "Your password can have at most 64 characters");
            isValid = false;
        }
        else if (checkConfirmedPassword.isEmpty() || !checkConfirmedPassword.equals(checkPassword)) {
            showError(confirm_password, "Your password doesn't match the previous one");
            isValid = false;
        }

        progress_bar.setVisibility(View.VISIBLE);

        // Connecting user data with Firebase
        if (isValid) {
            m_auth.createUserWithEmailAndPassword(checkEmailAddress, checkPassword)
                    .addOnCompleteListener(this, task -> {
                        progress_bar.setVisibility(ViewStub.GONE);

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterActivity.this, "You have successfully registered",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Authentication failed, please try again!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            progress_bar.setVisibility(ViewStub.GONE);
        }
    }

    private void showError(EditText input, String errorText) {
        input.setError(errorText);
        input.requestFocus();
    }
}