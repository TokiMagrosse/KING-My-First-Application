package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ProgressBar progress_bar;
    private FirebaseAuth m_auth;
    TextView register_text_reference, forgot_password;
    Button login_button;
    CheckBox remember_me;
    EditText email_address, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forgot_password = findViewById(R.id.forgot_password);
        login_button = findViewById(R.id.login_button);
        remember_me = findViewById(R.id.remember_me);
        email_address = findViewById(R.id.email_address_or_username);
        password = findViewById(R.id.password);
        progress_bar = findViewById(R.id.progress_bar);
        register_text_reference = findViewById(R.id.register_text_reference);
        m_auth = FirebaseAuth.getInstance();

        register_text_reference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });

    }

    private void checkCredentials() {
        String checkEmailAddress = email_address.getText().toString().trim();
        String checkPassword = password.getText().toString().trim();
        boolean isValid = true;

        if (checkEmailAddress.isEmpty()) {
            showError(email_address, "Please enter your email");
            isValid = false;
        }
        else if (!checkEmailAddress.contains("@") && !checkEmailAddress.contains(".")) {
            showError(email_address, "Please enter a valid email address");
            isValid = false;
        }
        else if (checkPassword.isEmpty()) {
            showError(password, "Please enter your password");
            isValid = false;
        }
        else if (checkPassword.length() < 8) {
            showError(password, "Your password length must be at least 8 characters");
            isValid = false;
        }
        else if (checkPassword.length() > 64) {
            showError(password, "Your password can have at most 64 characters");
            isValid = false;
        }

        progress_bar.setVisibility(ViewStub.VISIBLE);

        if (isValid) {
            m_auth.signInWithEmailAndPassword(checkEmailAddress, checkPassword)
                    .addOnCompleteListener(this, task -> {
                        progress_bar.setVisibility(ViewStub.GONE);

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "You have successfully logged in",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            startActivity(intent);
                            // finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid email or password",
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

