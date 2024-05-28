package com.example.poisonousking.outside_of_king;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poisonousking.R;
import com.example.poisonousking.helper_classes.HideTheBars;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    public View decorView;
    private EditText edit_text_email;
    private FirebaseAuth m_auth;
    TextView from_reset_to_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
            if (visibility == 0)
                decorView.setSystemUiVisibility(HideTheBars.hideSystemBars());
        });

        m_auth = FirebaseAuth.getInstance();
        edit_text_email = findViewById(R.id.edit_text_email);
        from_reset_to_login = findViewById(R.id.from_reset_to_login);
        Button reset_password_button = findViewById(R.id.reset_password_button);

        from_reset_to_login.setOnClickListener(v -> {
            Intent intent = new Intent(ResetPasswordActivity.this, LogInActivity.class);
            startActivity(intent);
        });

        reset_password_button.setOnClickListener(v -> resetPassword());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            decorView.setSystemUiVisibility(HideTheBars.hideSystemBars());
    }

    private void resetPassword() {
        String email = edit_text_email.getText().toString().trim();
        boolean isValid = true;

        if (email.isEmpty()) {
            showError(edit_text_email, "Please enter your email.");
            isValid = false;
        } else if (!email.contains("@") && !email.contains(".") || !email.contains("@") || !email.contains(".")) {
            showError(edit_text_email, "Please enter a valid email address.");
            isValid = false;
        }

        if (isValid) {
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
    }

    private void showError(@NonNull EditText input, String errorText) {
        input.setError(errorText);
        input.requestFocus();
    }
}