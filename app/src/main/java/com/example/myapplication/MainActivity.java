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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // ProgressBar progress_bar;
    private FirebaseAuth m_auth;
    TextView register_text_reference, forgot_password;
    Button login_button;
    CheckBox remember_me;
    EditText email_address, password;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = m_auth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // progress_bar = findViewById(R.id.progress_bar);
        m_auth = FirebaseAuth.getInstance();
        register_text_reference = findViewById(R.id.register_text_reference);
        register_text_reference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
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

    }

    private void checkCredentials() {
        // progress_bar.setVisibility(ViewStub.VISIBLE);
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

        m_auth.signInWithEmailAndPassword(checkEmailAddress, checkPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // progress_bar.setVisibility(ViewStub.GONE);

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "You have successfully logged in",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                            startActivity(intent);
                            finish();
                        } /*else {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }*/
                    }
                });
    }

    private void showError(EditText input, String errorText) {
        input.setError(errorText);
        input.requestFocus();
    }

}

