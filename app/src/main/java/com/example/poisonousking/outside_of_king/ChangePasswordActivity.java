package com.example.poisonousking.outside_of_king;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.poisonousking.R;
import com.example.poisonousking.helper_classes.HideTheBars;
import com.example.poisonousking.inside_of_king.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ChangePasswordActivity extends AppCompatActivity {

    public View decorView;
    private EditText edit_text_email;
    private FirebaseAuth m_auth;
    TextView from_reset_to_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
            Intent intent = new Intent(ChangePasswordActivity.this, ProfileActivity.class);
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
                            Toast.makeText(ChangePasswordActivity.this,
                                    "Password reset email sent. Check your email inbox.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChangePasswordActivity.this,
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