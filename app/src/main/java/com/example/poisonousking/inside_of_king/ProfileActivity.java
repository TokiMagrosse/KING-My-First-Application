package com.example.poisonousking.inside_of_king;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.bumptech.glide.Glide;
import com.example.poisonousking.R;
import com.example.poisonousking.outside_of_king.ChangePasswordActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    Dialog dialog_change_profile, dialog_change_username;
    EditText new_username;
    Button change_username, change_email, change_password, change_image, close_change_dialog;
    Button apply_new_username, close_username_dialog;
    // Define the volume level you want (0.0 - 1.0 range)
    MediaPlayer buttonClickSound;
    private static final float BACKGROUND_MUSIC_VOLUME = 0.35f; // Set volume level to 35% for background music
    Uri selected_image_uri;
    ActivityResultLauncher<Intent> image_pick_launcher;
    StorageReference storageReference;
    FirebaseUser user;
    FirebaseAuth f_auth;
    FirebaseFirestore f_store;
    TextView rating, rank, p_coins, total_games, wins, loses;
    String title;
    private int total_games_count, won_games_count, lost_games_count, coins_count, rating_number;
    ImageView profile_picture;
    static String userID;
    Button change_profile, save_changes, back_to_main;
    TextView username, email_address, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeViews();
        fetchAndDisplayProfileImage();
    }

    private void initializeViews() {
        f_auth = FirebaseAuth.getInstance();
        f_store = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        user = f_auth.getCurrentUser();
        userID = Objects.requireNonNull(user).getUid();
        rating = findViewById(R.id.rating);
        rank = findViewById(R.id.rank);
        p_coins = findViewById(R.id.level);
        total_games = findViewById(R.id.total_games);
        wins = findViewById(R.id.wins);
        loses = findViewById(R.id.loses);
        profile_picture = findViewById(R.id.your_profile_picture);
        change_profile = findViewById(R.id.change_profile_button);
        save_changes = findViewById(R.id.my_progress_button);
        back_to_main = findViewById(R.id.back_to_main_button);
        username = findViewById(R.id.username_in_profile);
        email_address = findViewById(R.id.email_in_profile);
        id = findViewById(R.id.id_in_profile);

        getUserStats(userID);

        dialog_change_profile = new Dialog(ProfileActivity.this);
        dialog_change_profile.setContentView(R.layout.dialog_change_profile);
        Objects.requireNonNull(dialog_change_profile.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_change_profile.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_dialog_bg));
        dialog_change_profile.setCancelable(false);

        Window change_profile_window = dialog_change_profile.getWindow();
        if (change_profile_window != null) {
            change_profile_window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            change_profile_window.setGravity(Gravity.CENTER); // Set the gravity to top
            change_profile_window.setWindowAnimations(R.style.DialogAnimation); // Set the animation
        }

        change_username = dialog_change_profile.findViewById(R.id.change_users_username);
        change_email = dialog_change_profile.findViewById(R.id.change_users_email_address);
        change_password = dialog_change_profile.findViewById(R.id.change_users_password);
        change_image = dialog_change_profile.findViewById(R.id.change_users_profile_image);
        close_change_dialog = dialog_change_profile.findViewById(R.id.close_change_profile_dialog);

        dialog_change_username = new Dialog(ProfileActivity.this);
        dialog_change_username.setContentView(R.layout.dialog_change_username);
        Objects.requireNonNull(dialog_change_username.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_change_username.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_dialog_bg));
        dialog_change_username.setCancelable(false);

        Window change_email_window = dialog_change_username.getWindow();
        if (change_email_window != null) {
            change_email_window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            change_email_window.setGravity(Gravity.CENTER); // Set the gravity to top
            change_email_window.setWindowAnimations(R.style.DialogAnimation); // Set the animation
        }

        new_username = dialog_change_username.findViewById(R.id.new_username);
        apply_new_username = dialog_change_username.findViewById(R.id.apply_new_username);
        close_username_dialog = dialog_change_username.findViewById(R.id.close_username_dialog);

        apply_new_username.setOnClickListener(v -> {
            buttonClickSound.start();
            applyNewUsername(new_username);
        });

        change_username.setOnClickListener(v -> {
            buttonClickSound.start();
            dialog_change_username.show();
        });

        close_username_dialog.setOnClickListener(v -> {
            buttonClickSound.start();
            dialog_change_username.dismiss();
        });

        close_change_dialog.setOnClickListener(v -> {
            buttonClickSound.start();
            dialog_change_profile.dismiss();
        });
        change_image.setOnClickListener(v -> {
            buttonClickSound.start();
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(518)
                    .maxResultSize(518, 518)
                    .createIntent(intent -> {
                        image_pick_launcher.launch(intent);
                        return null;
                    });
        });
        change_password.setOnClickListener(task -> {
            Intent i = new Intent(this, ChangePasswordActivity.class);
            startActivity(i);
        });

        buttonClickSound = MediaPlayer.create(this, R.raw.button_click_sound_1);
        // Set the volume of the mediaPlayer to a lower level (background music volume)
        buttonClickSound.setVolume(BACKGROUND_MUSIC_VOLUME, BACKGROUND_MUSIC_VOLUME);

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
            buttonClickSound.start();
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        // Handle profile picture change
        change_profile.setOnClickListener(v -> {
            buttonClickSound.start();
            dialog_change_profile.show();
        });

        // Handle saving changes
        save_changes.setOnClickListener(v -> {
            buttonClickSound.start();
            if (selected_image_uri != null) {
                // Upload the selected image to Firebase Storage
                uploadProfileImageToStorage(selected_image_uri);
            } else {
                Toast.makeText(this, "You haven't changed anything", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyNewUsername(@NonNull EditText newUsername) {
        String checkUsername = newUsername.getText().toString().trim();

        boolean isValid = true;

        if (checkUsername.isEmpty()) {
            showError(newUsername, "Please enter your username");
            isValid = false;
        }
        else if (checkUsername.contains("-") ||
                checkUsername.contains("*") || checkUsername.contains("&") ||
                checkUsername.contains("#") || checkUsername.contains("%") ||
                checkUsername.contains("@") || checkUsername.contains("$") ||
                checkUsername.contains("|") || checkUsername.contains("/") ||
                checkUsername.contains("^") || checkUsername.contains("?") ||
                checkUsername.contains("(") || checkUsername.contains(")") ||
                checkUsername.contains("+") || checkUsername.contains("=") ||
                checkUsername.contains("<") || checkUsername.contains(">") ||
                checkUsername.contains(",") || checkUsername.contains("'")) {
            showError(newUsername, "Your username can contain only specific character underscore '_'");
            isValid = false;
        }
        else if (checkUsername.contains(" ")) {
            showError(newUsername, "Your username should not contain spaces");
            isValid = false;
        }
        else if (checkUsername.length() < 5 || checkUsername.length() > 23) {
            showError(newUsername, "Your username length range is from 5 characters to 23");
            isValid = false;
        }

        if (isValid) {
            updateUsername(checkUsername);
        }
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

    private void getUserStats(String documentId) {
        DocumentReference gameCountDocRef = f_store.collection("all my users").document(documentId);
        gameCountDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    title = Objects.requireNonNull(document.getString("Title"));
                    rating_number = Objects.requireNonNull(document.getLong("Rating")).intValue();
                    coins_count = Objects.requireNonNull(document.getLong("Coins")).intValue();
                    total_games_count = Objects.requireNonNull(document.getLong("Total games count")).intValue();
                    won_games_count = Objects.requireNonNull(document.getLong("Won games count")).intValue();
                    lost_games_count = Objects.requireNonNull(document.getLong("Lost games count")).intValue();

                    // Update the TextViews with the fetched data on the main thread
                    runOnUiThread(() -> {
                        rank.setText(title);
                        rating.setText(String.valueOf(rating_number));
                        p_coins.setText(String.valueOf(coins_count));
                        total_games.setText(String.valueOf(total_games_count));
                        wins.setText(String.valueOf(won_games_count));
                        loses.setText(String.valueOf(lost_games_count));
                    });

                } else {
                    runOnUiThread(() -> Toast.makeText(this, "No such document", Toast.LENGTH_SHORT).show());
                }
            } else {
                Log.d("ProfileActivity", "get failed with ", task.getException());
            }
        });
    }

    private void showError(@NonNull EditText input, String errorText) {
        input.setError(errorText);
        input.requestFocus();
    }

    private void updateUsername(String newUsername) {
        FirebaseUser user = f_auth.getCurrentUser();

        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newUsername)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Username updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed to update username", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}