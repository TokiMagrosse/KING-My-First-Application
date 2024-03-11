package com.example.poisonousking;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poisonousking.ProfileActivity;
import com.example.poisonousking.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class UserProfileAttributesActivity extends AppCompatActivity {

    StorageReference storage_reference;
    FirebaseUser user;
    FirebaseAuth f_auth;
    FirebaseFirestore f_store;
    TextView rating, rank, level, wins, loses;
    ImageView profile_picture;
    String userID;
    Button change_profile, save_changes, back_to_main;
    TextView username, email_address, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_attributes);

        f_auth = FirebaseAuth.getInstance();
        f_store = FirebaseFirestore.getInstance();
        storage_reference = FirebaseStorage.getInstance().getReference();
        user = f_auth.getCurrentUser();
        userID = Objects.requireNonNull(f_auth.getCurrentUser()).getUid();
        rating = findViewById(R.id.rating);
        rank = findViewById(R.id.rank);
        level = findViewById(R.id.level);
        wins = findViewById(R.id.wins);
        loses = findViewById(R.id.loses);
        profile_picture = findViewById(R.id.your_profile_picture);
        change_profile = findViewById(R.id.change_profile_button);
        save_changes = findViewById(R.id.my_progress_button);
        back_to_main = findViewById(R.id.back_to_main_button);
        username = findViewById(R.id.username_in_profile);
        email_address = findViewById(R.id.email_in_profile);
        id = findViewById(R.id.id_in_profile);

        // Assuming you have obtained user data after registration
        String userEmail = user.getEmail();

        DocumentReference doc_ref_for_username = f_store.collection("all my users").document(user.getUid());
        doc_ref_for_username.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String my_username = documentSnapshot.getString("Username");
                username.setText(my_username);
            } else {
                // Handle the case where user data is not found
                username.setText("@#*(%&^$^@");
            }
        }).addOnFailureListener(e -> {
            // Handle the failure to retrieve user data
            username.setText("@)$&%*%@^$");
        });

        DocumentReference doc_ref_for_id = f_store.collection("all my users").document(user.getUid());
        doc_ref_for_id.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String my_id = documentSnapshot.getString("Personal ID");
                id.setText(my_id);
            }
        });

        email_address.setText(userEmail);

        // Setting the ImageView to be square
        profile_picture.post(() -> {
            profile_picture.getLayoutParams().height = profile_picture.getWidth();
        });

        back_to_main.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileAttributesActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        change_profile.setOnClickListener(v -> {
            // Open the gallery
            Intent open_gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(open_gallery_intent, 2389);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2389 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri image_uri = data.getData();
                if (image_uri != null) {
                    uploadImageToFirebase(image_uri);
                } else {
                    Toast.makeText(this, "Failed to obtain image URI", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImageToFirebase(Uri image_uri) {
        // Uploading image to Firebase storage
        StorageReference file_reference = storage_reference.child("king-of-hearts-red-min_1200x1200.jpg");
        file_reference.putFile(image_uri)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(UserProfileAttributesActivity.this, "Your image uploaded", Toast.LENGTH_SHORT).show();
                    file_reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        Picasso.get().load(uri).into(profile_picture);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UserProfileAttributesActivity.this, "Failed to upload your image", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error uploading image: ", e);
                });
    }

}

