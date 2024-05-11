package com.example.poisonousking.inside_of_king;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.poisonousking.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    Uri selected_image_uri;
    ActivityResultLauncher<Intent> image_pick_launcher;
    StorageReference storageReference;
    FirebaseUser user;
    FirebaseAuth f_auth;
    FirebaseFirestore f_store;
    TextView rating, rank, level, wins, loses;
    ImageView profile_picture;
    static String userID;
    Button change_profile, save_changes, back_to_main;
    TextView username, email_address, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        f_auth = FirebaseAuth.getInstance();
        f_store = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
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

        // Logic of setting profile image
        image_pick_launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selected_image_uri = data.getData();
                            setProfilePicture(getApplicationContext(), selected_image_uri, profile_picture);
                        }
                    }
                });

        // Fetch and set the user's email
        String userEmail = user.getEmail();
        email_address.setText(userEmail);

        // Fetch and set the user's username from Fire_store
        DocumentReference doc_ref_for_username = f_store.collection("all my users").document(user.getUid());
        doc_ref_for_username.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String my_username = documentSnapshot.getString("Username");
                username.setText(my_username);
            } else {
                // Handle the case where user data is not found
                username.setText("*****");
            }
        }).addOnFailureListener(e -> {
            // Handle the failure to retrieve user data
            username.setText("*****");
        });

        // Fetch and set the user's ID from Fire_store
        DocumentReference doc_ref_for_id = f_store.collection("all my users").document(user.getUid());
        doc_ref_for_id.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String my_id = documentSnapshot.getString("Personal ID");
                id.setText(my_id);
            }
        });

        // Set up the back to main button
        back_to_main.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        // Handle profile picture change
        change_profile.setOnClickListener(v -> ImagePicker.with(this)
                .cropSquare()
                .compress(518)
                .maxResultSize(518, 518)
                .createIntent(intent -> {
                    image_pick_launcher.launch(intent);
                    return null;
                }));

        // Handle saving changes
        save_changes.setOnClickListener(v -> {
            if (selected_image_uri != null) {
                // Upload the selected image to Firebase Storage
                uploadProfileImageToStorage(selected_image_uri);
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        });

        fetchAndDisplayProfileImage();
    }

    // Method to set the profile picture in the ImageView
    private static void setProfilePicture(Context context, Uri imageUri, ImageView imageView) {
        Glide.with(context).load(imageUri).into(imageView);
    }

    // Method to upload the selected profile image to Firebase Storage
    private void uploadProfileImageToStorage(Uri imageUri) {
        // Define the file path based on your rules
        String filePath = "files/" + userID + "/profile_picture.jpg";
        StorageReference profilePicRef = storageReference.child(filePath);

        // Log the file path and user ID for verification
        Log.d("ProfileImageUpload", "File path: " + filePath);
        Log.d("ProfileImageUpload", "User ID: " + userID);

        // Start uploading the file
        profilePicRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL after the image is successfully uploaded
                    profilePicRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Update the download URL in Fire_store
                        updateProfileImageUrlInFirebaseStorage(uri.toString());
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle the failure to upload the image
                    Log.e("ProfileImageUpload", "Failed to upload profile image: ", e);
                    Toast.makeText(this, "Failed to upload profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    // Method to update the profile image URL in Fire_store
    private void updateProfileImageUrlInFirebaseStorage(String imageUrl) {
        DocumentReference userDocRef = FirebaseFirestore.getInstance().collection("all my users").document(userID);

        userDocRef.update("profileImageUrl", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // Successfully updated the profile image URL
                    Toast.makeText(this, "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Failed to update the profile image URL
                    Toast.makeText(this, "Failed to update profile image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchAndDisplayProfileImage() {
        DocumentReference userDocRef = FirebaseFirestore.getInstance().collection("all my users").document(userID);

        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String imageUrl = documentSnapshot.getString("profileImageUrl");
                if (imageUrl != null) {
                    // Load and display the profile image using Glide
                    Glide.with(this)
                            .load(imageUrl)
                            .into(profile_picture);
                }
            }
        }).addOnFailureListener(e -> {
            // Handle failure to fetch profile image URL
            Toast.makeText(this, "Failed to fetch profile image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

}
