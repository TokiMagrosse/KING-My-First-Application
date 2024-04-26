package com.example.poisonousking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class UserProfileAttributesActivity extends AppCompatActivity {

    StorageReference storageReference;
    Uri imageUri;
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

        // Assuming you have obtained user data after registration
        String userEmail = user.getEmail();

        // Fetch and set the user's username from Firestore
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

        // Fetch and set the user's ID from Firestore
        DocumentReference doc_ref_for_id = f_store.collection("all my users").document(user.getUid());
        doc_ref_for_id.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String my_id = documentSnapshot.getString("Personal ID");
                id.setText(my_id);
            }
        });

        email_address.setText(userEmail);

        back_to_main.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileAttributesActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        change_profile.setOnClickListener(v -> {
            // Create an intent to open the gallery
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Check and request permission for accessing external storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        }

        /*save_changes.setOnClickListener(v -> {
            // Upload the profile picture
            uploadImageToFirebaseStorage(profile_picture);
        });*/
    }

    // Define a constant for the image pick request
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the URI of the selected image
            Uri imageUri = data.getData();

            // Set the selected image to the profile picture ImageView
            profile_picture.setImageURI(imageUri);
        }
    }

    // Upload image to Firebase Storage
    private void uploadImageToFirebaseStorage(@NonNull ImageView imageView) {
        // Get the drawable from the ImageView
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            // Convert drawable to bitmap
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            // Convert bitmap to Uri
            Uri imageUri = getImageUri(this, bitmap);

            // Proceed with uploading image to Firebase Storage
            StorageReference profileImageRef = storageReference.child("profile_images/" + userID + ".jpg");

            profileImageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully
                        profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Save the download URL to Firestore
                            saveDownloadUrlToFirebaseStorage(uri.toString());

                            Toast.makeText(UserProfileAttributesActivity.this, "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(exception -> {
                            // Handle any errors
                            Toast.makeText(UserProfileAttributesActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(exception -> {
                        // Handle unsuccessful uploads
                        Toast.makeText(UserProfileAttributesActivity.this, "Failed to upload profile image", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void saveDownloadUrlToFirebaseStorage(String downloadUrl) {
        // Save the download URL to Firestore
        DocumentReference userDocRef = f_store.collection("all my users").document(userID);
        userDocRef.update("profile_picture_url", downloadUrl)
                .addOnSuccessListener(aVoid -> {
                    // URL saved successfully
                    Toast.makeText(UserProfileAttributesActivity.this, "Profile picture URL saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Toast.makeText(UserProfileAttributesActivity.this, "Failed to save profile picture URL", Toast.LENGTH_SHORT).show();
                });
    }

    // Define a constant for the permission request
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 2;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the operation that requires the permission
                // For example, you can re-trigger the image selection process here
            } else {
                // Permission denied, handle the case where the user denies the permission
            }
        }
    }

    // Method to convert Bitmap to Uri
    public Uri getImageUri(@NonNull Context inContext, @NonNull Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}




