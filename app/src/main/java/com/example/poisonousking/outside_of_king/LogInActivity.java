package com.example.poisonousking.outside_of_king;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poisonousking.helper_classes.HideTheBars;
import com.example.poisonousking.inside_of_king.HomeActivity;
import com.example.poisonousking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    public View decorView;
    ProgressBar progressBar;
    public byte passwordLength;
    private FirebaseAuth mAuth;
    TextView registerTextReference, forgotPassword;
    Button loginButton;
    CheckBox rememberMe;
    EditText emailAddress, password;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
            if (visibility == 0)
                decorView.setSystemUiVisibility(HideTheBars.hideSystemBars());
        });

        forgotPassword = findViewById(R.id.forgot_password);
        loginButton = findViewById(R.id.login_button);
        emailAddress = findViewById(R.id.email_address_or_username);
        password = findViewById(R.id.password);
        rememberMe = findViewById(R.id.remember_me);
        progressBar = findViewById(R.id.progress_bar);
        registerTextReference = findViewById(R.id.register_text_reference);
        mAuth = FirebaseAuth.getInstance();

        registerTextReference.setOnClickListener(v -> {
            Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> checkCredentials());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            decorView.setSystemUiVisibility(HideTheBars.hideSystemBars());
    }

    private void checkCredentials() {
        String checkEmailAddress = emailAddress.getText().toString().trim();
        String checkPassword = password.getText().toString();
        boolean isValid = true;

        if (checkEmailAddress.isEmpty()) {
            showError(emailAddress, "Please enter your email");
            isValid = false;
        } else if (!checkEmailAddress.contains("@") && !checkEmailAddress.contains(".")) {
            showError(emailAddress, "Please enter a valid email address");
            isValid = false;
        } else if (checkPassword.isEmpty()) {
            showError(password, "Please enter your password");
            isValid = false;
        } else if (checkPassword.length() < 8) {
            showError(password, "Your password length must be at least 8 characters");
            isValid = false;
        } else if (checkPassword.contains(" ")) {
            showError(password, "Your password should not contain spaces");
            isValid = false;
        } else if (checkPassword.length() > 64) {
            showError(password, "Your password can have at most 64 characters");
            isValid = false;
        }

        progressBar.setVisibility(ViewStub.VISIBLE);

        if (isValid) {
            mAuth.signInWithEmailAndPassword(checkEmailAddress, checkPassword)
                    .addOnCompleteListener(this, task -> {
                        progressBar.setVisibility(ViewStub.GONE);

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(LogInActivity.this, "You have successfully logged in",
                                        Toast.LENGTH_SHORT).show();
                                passwordLength = (byte) checkPassword.length();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            } else {
                                // Email is not verified
                                Toast.makeText(LogInActivity.this, "Please verify your email address",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LogInActivity.this, "Invalid email or password, please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            progressBar.setVisibility(ViewStub.GONE);
        }

        forgotPassword.setOnClickListener(v -> openForgotPasswordActivity());
    }

    private void showError(@NonNull EditText input, String errorText) {
        input.setError(errorText);
        input.requestFocus();
    }

    private void openForgotPasswordActivity() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }
}
