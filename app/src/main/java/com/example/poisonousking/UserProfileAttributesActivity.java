package com.example.poisonousking;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class UserProfileAttributesActivity extends AppCompatActivity {

    StorageReference storage_reference;
    private static final int PICK_IMAGE_REQUEST = 1;
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
            startActivityForResult(open_gallery_intent, PICK_IMAGE_REQUEST);
        });
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI
            Uri selectedImageUri = data.getData();

            // Generate a unique file name for the cropped image
            String uniqueFileName = "croppedImage_" + System.currentTimeMillis() + ".jpg";
            Uri destinationUri = Uri.fromFile(new File(getContext().getCacheDir(), uniqueFileName));

            // Start UCrop activity with the selected image URI and destination URI
            UCrop.of(selectedImageUri, destinationUri)
                    .withAspectRatio(1, 1)  // Set the aspect ratio as needed
                    .start(getContext(), this);
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            // Handle the result after cropping
            assert data != null;
            final Uri resultUri = UCrop.getOutput(data);

            // Check for null before setting the image
            if (resultUri != null) {
                profile_picture.setImageURI(resultUri);

                // Upload the cropped image to Firebase Cloud Storage
                StorageReference fileReference = storage_reference.child("profile_images/" + System.currentTimeMillis() + ".jpg");
                fileReference.putFile(resultUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Get the download URL of the uploaded image
                            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Store the download URL in the User object in Firebase Realtime Database
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    String uid = user.getUid();
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                    databaseReference.child("users").child(uid).child("profileImageUrl").setValue(uri.toString());
                                }
                            });
                        })
                        .addOnFailureListener(e -> {
                            // Handle error
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            // Handle the error that occurred during cropping (if needed)
            final Throwable cropError = UCrop.getError(data);
        }
    }*/

    /*private void uploadImageToFirebase(Uri imageUri) {
        // Resize the image
        Picasso.get()
                .load(imageUri)
                .resize(300, 300) // Set desired width and height here
                .centerCrop()
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        // Upload the resized image to Firebase Storage
                        ByteArrayOutputStream ba_os = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ba_os);
                        byte[] imageData = ba_os.toByteArray();

                        // Create a reference to the image file in Firebase Storage
                        StorageReference fileReference = storage_reference.child("profile_picture.jpg");

                        // Upload the image data
                        fileReference.putBytes(imageData)
                                .addOnSuccessListener(taskSnapshot -> {
                                    Toast.makeText(UserProfileAttributesActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(UserProfileAttributesActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Error uploading image: ", e);
                                });
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Toast.makeText(UserProfileAttributesActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        // Do nothing
                    }
                });
    }*/

}

