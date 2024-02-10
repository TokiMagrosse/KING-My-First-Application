package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileAttributesActivity extends AppCompatActivity {

    Button resend_code;
    FirebaseAuth f_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_attributes);

        resend_code = findViewById(R.id.verify_email_button);
        final FirebaseUser user = f_auth.getCurrentUser();

        assert user != null;
        if (!user.isEmailVerified()) {
            resend_code.setVisibility(View.VISIBLE);

            resend_code.setOnClickListener(v -> {
                // Send verification link to email

                user.sendEmailVerification()
                        .addOnSuccessListener(unused -> Toast.makeText(UserProfileAttributesActivity.this, "Email verification link sent to your email", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Log.d(TAG, "Email not sent" + e.getMessage()));
            });
        }
    }
}