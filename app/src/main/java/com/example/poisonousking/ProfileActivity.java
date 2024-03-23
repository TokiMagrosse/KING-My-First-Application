package com.example.poisonousking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
// import androidx.fragment.app.Fragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button log_out_button, play_button, menu_button;
    ImageView your_profile_picture;
    TextView your_username;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*findViewById(R.id.menu_button).setOnClickListener(v -> {
            // Create and add the MenuFragment dynamically
            MenuFragment menuFragment = new MenuFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            transaction.add(R.id.menu_fragment, menuFragment, "MenuFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        });*/

        FirebaseFirestore f_store = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        log_out_button = findViewById(R.id.log_out_button);
        your_profile_picture = findViewById(R.id.your_profile_picture);
        your_username = findViewById(R.id.your_username);
        play_button = findViewById(R.id.play_button);
        menu_button = findViewById(R.id.menu_button);
        user = auth.getCurrentUser();

        play_button.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, GameFieldActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Your game will start soon. Good luck!", Toast.LENGTH_LONG).show();
        });

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