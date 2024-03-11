package com.example.poisonousking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button log_out_button;
    ImageView your_profile_picture;
    TextView your_username;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseFirestore f_store = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        log_out_button = findViewById(R.id.log_out_button);
        your_profile_picture = findViewById(R.id.your_profile_picture);
        your_username = findViewById(R.id.your_username);
        user = auth.getCurrentUser();

        your_profile_picture.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, UserProfileAttributesActivity.class);
            startActivity(intent);
        });

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            DocumentReference documentReference = f_store.collection("all my users").document(user.getUid());
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String username = documentSnapshot.getString("Username");
                    your_username.setText(username);
                } else {
                    // Handle the case where user data is not found
                    your_username.setText("**********");
                }
            }).addOnFailureListener(e -> {
                // Handle the failure to retrieve user data
                your_username.setText("**********");
            });
        }

        log_out_button.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });

    }
}