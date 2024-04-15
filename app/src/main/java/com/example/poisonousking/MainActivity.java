package com.example.poisonousking;

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
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    @SuppressLint("ClickableViewAccessibility")
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

        // Check if "Remember me" checkbox was previously checked
        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean rememberMeChecked = preferences.getBoolean("rememberMe", false);
        if (rememberMeChecked) {
            // Restore email and password from shared preferences
            String savedEmail = preferences.getString("email", "");
            String savedPassword = preferences.getString("password", "");
            email_address.setText(savedEmail);
            password.setText(savedPassword);
            remember_me.setChecked(true);
        }

        register_text_reference.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        remember_me.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save "Remember me" checkbox state
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("rememberMe", isChecked);
            editor.apply();
        });

        // Set both start and end drawables programmatically
        Drawable lockDrawable = ContextCompat.getDrawable(this, R.drawable.password_logo_icon_small);
        Drawable visibilityOffDrawable = ContextCompat.getDrawable(this, R.drawable.visibility_off_icon);
        Drawable visibilityOnDrawable = ContextCompat.getDrawable(this, R.drawable.visibility_on_icon);
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

        login_button.setOnClickListener(v -> checkCredentials());
    }

    private void checkCredentials() {
        String checkEmailAddress = email_address.getText().toString().trim();
        String checkPassword = password.getText().toString();
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
        else if (checkPassword.contains(" ")) {
            showError(password, "Your password should not contain spaces");
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
                            FirebaseUser user = m_auth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(MainActivity.this, "You have successfully logged in",
                                        Toast.LENGTH_SHORT).show();
                                password_length = (byte) checkPassword.length();
                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                startActivity(intent);
                                // finish();
                            } else {
                                // Email is not verified
                                Toast.makeText(MainActivity.this, "Please verify your email address",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid email or password, please try again",
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

    private void showError(@NonNull EditText input, String errorText) {
        input.setError(errorText);
        input.requestFocus();
    }

    private void openForgotPasswordActivity() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }
}

