package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class UserProfileAttributesActivity extends AppCompatActivity {

    FirebaseUser user;
    FirebaseAuth f_auth;
    FirebaseFirestore f_store;
    TextView rating, rank, level, coins;
    ImageView profile_picture;
    String userID;
    Button change_profile, my_progress, back_to_main;
    TextView username, email_address, password, country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_attributes);
        f_auth = FirebaseAuth.getInstance();
        f_store = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(f_auth.getCurrentUser()).getUid();


        rating = findViewById(R.id.rating);
        rank = findViewById(R.id.rank);
        level = findViewById(R.id.level);
        coins = findViewById(R.id.coins);
        profile_picture = findViewById(R.id.your_profile_picture);
        change_profile = findViewById(R.id.change_profile_button);
        my_progress = findViewById(R.id.my_progress_button);
        back_to_main = findViewById(R.id.back_to_main_button);
        username = findViewById(R.id.username_in_profile);
        email_address = findViewById(R.id.email_in_profile);
        password = findViewById(R.id.password_in_profile);
        country = findViewById(R.id.country_in_profile);

        back_to_main.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileAttributesActivity.this, ProfileActivity.class);
            startActivity(intent);
        });


    }
}