package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText edit_text_email;
    private FirebaseAuth m_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        edit_text_email = findViewById(R.id.edit_text_email);
        Button reset_password_button = findViewById(R.id.reset_password_button);

        reset_password_button.setOnClickListener(v -> {
            checkCredentials();
            resetPassword();
        });
    }

    private void resetPassword() {
        String email = edit_text_email.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        m_auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this,
                                "Password reset email sent. Check your email inbox.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this,
                                "Failed to send password reset email. Check your email address.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkCredentials() {
        String checkEmailAddress = edit_text_email.getText().toString().trim();
        // boolean isValid = true;

        if (checkEmailAddress.isEmpty()) {
            showError(edit_text_email, "Please enter your email");
            // isValid = false;
        } else if (!checkEmailAddress.contains("@") && !checkEmailAddress.contains(".")) {
            showError(edit_text_email, "Please enter a valid email address");
            // isValid = false;
        }
    }

    private void showError(EditText input, String errorText) {
        input.setError(errorText);
        input.requestFocus();
    }
}