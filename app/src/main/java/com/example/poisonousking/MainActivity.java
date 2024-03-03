package com.example.poisonousking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ProgressBar progress_bar;
    public byte password_length;
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

        register_text_reference.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        login_button.setOnClickListener(v -> checkCredentials());

    }

    private void checkCredentials() {
        String checkEmailAddress = email_address.getText().toString().trim();
        String checkPassword = password.getText().toString();
        boolean isValid = true;

        if (checkEmailAddress.isEmpty()) {
            showError(email_address, "Please enter your email.");
            isValid = false;
        }
        else if (!checkEmailAddress.contains("@") && !checkEmailAddress.contains(".")) {
            showError(email_address, "Please enter a valid email address.");
            isValid = false;
        }
        else if (checkPassword.isEmpty()) {
            showError(password, "Please enter your password.");
            isValid = false;
        }
        else if (checkPassword.length() < 8) {
            showError(password, "Your password length must be at least 8 characters.");
            isValid = false;
        }
        else if (checkPassword.contains(" ")) {
            showError(password, "Your password should not contain spaces.");
            isValid = false;
        }
        else if (checkPassword.length() > 64) {
            showError(password, "Your password can have at most 64 characters.");
            isValid = false;
        }


        progress_bar.setVisibility(ViewStub.VISIBLE);

        if (isValid) {
            m_auth.signInWithEmailAndPassword(checkEmailAddress, checkPassword)
                    .addOnCompleteListener(this, task -> {
                        progress_bar.setVisibility(ViewStub.GONE);

                        if (task.isSuccessful()) {
                            FirebaseUser user = m_auth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(MainActivity.this, "You have successfully logged in.",
                                        Toast.LENGTH_SHORT).show();
                                password_length = (byte) checkPassword.length();
                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                startActivity(intent);
                                // finish();
                            } else {
                                // Email is not verified
                                Toast.makeText(MainActivity.this, "Please verify your email address.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid email or password, please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            progress_bar.setVisibility(ViewStub.GONE);
        }

        forgot_password.setOnClickListener(v -> {
            openForgotPasswordActivity();
        });
    }

    private void showError(EditText input, String errorText) {
        input.setError(errorText);
        input.requestFocus();
    }

    private void openForgotPasswordActivity() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

}


