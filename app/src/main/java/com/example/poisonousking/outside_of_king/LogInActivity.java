package com.example.poisonousking.outside_of_king;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poisonousking.inside_of_king.HomeActivity;
import com.example.poisonousking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    ProgressBar progressBar;
    public byte passwordLength;
    private FirebaseAuth mAuth;
    TextView registerTextReference, forgotPassword;
    Button loginButton;
    CheckBox rememberMe;
    EditText emailAddress, password;
    public static final String SHARED_PREFS = "sharedPrefs";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        forgotPassword = findViewById(R.id.forgot_password);
        loginButton = findViewById(R.id.login_button);
        rememberMe = findViewById(R.id.remember_me);
        emailAddress = findViewById(R.id.email_address_or_username);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress_bar);
        registerTextReference = findViewById(R.id.register_text_reference);
        mAuth = FirebaseAuth.getInstance();

        registerTextReference.setOnClickListener(v -> {
            Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Set both start and end drawables programmatically
        Drawable lockDrawable = ContextCompat.getDrawable(this, R.drawable.password_logo_icon_small);
        Drawable visibilityOffDrawable = ContextCompat.getDrawable(this, R.drawable.visibility_off_icon_small);
        Drawable visibilityOnDrawable = ContextCompat.getDrawable(this, R.drawable.visibility_on_icon_small);
        password.setCompoundDrawablesRelativeWithIntrinsicBounds(lockDrawable, null, visibilityOffDrawable, null);

        // Set touch listener for the visibility toggle
        final boolean[] isVisible = {false}; // Variable to track password visibility
        password.setOnTouchListener((v, event) -> {
            final int right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableWidth = password.getCompoundDrawables()[right].getBounds().width();
                if (drawableWidth > 0) { // Ensure the compound drawable exists
                    int drawableRight = password.getRight() - password.getPaddingRight(); // Adjusted calculation for right position
                    if (event.getRawX() >= drawableRight - drawableWidth) {
                        int selection = password.getSelectionEnd();
                        if (isVisible[0]) {
                            // Setting visibility eye drawable image here
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(lockDrawable, null, visibilityOffDrawable, null);
                            // For hide password
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isVisible[0] = false;
                        } else {
                            // Setting visibility eye drawable image here
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(lockDrawable, null, visibilityOnDrawable, null);
                            // For showing password
                            password.setTransformationMethod(null); // Set null to show the password
                            isVisible[0] = true;
                        }
                        password.setSelection(selection);
                        return true;
                    }
                }
            }
            return false;
        });

        loginButton.setOnClickListener(v -> checkCredentials());
    }

    private void checkCredentials() {
        String checkEmailAddress = emailAddress.getText().toString().trim();
        String checkPassword = password.getText().toString();
        boolean isValid = true;

        if (checkEmailAddress.isEmpty()) {
            showError(emailAddress, "Please enter your email");
            isValid = false;
        }
        else if (!checkEmailAddress.contains("@") && !checkEmailAddress.contains(".")) {
            showError(emailAddress, "Please enter a valid email address");
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
        else if (checkPassword.contains(" ")) {
            showError(password, "Your password should not contain spaces");
            isValid = false;
        }
        else if (checkPassword.length() > 64) {
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
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("name", "true");
                                editor.apply();
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
        }
        else {
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

