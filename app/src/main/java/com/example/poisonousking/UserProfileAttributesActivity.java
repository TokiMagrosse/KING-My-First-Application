package com.example.poisonousking;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.poisonousking.auxiliaryclasses.UserModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class UserProfileAttributesActivity extends AppCompatActivity {

    Uri selected_image_uri;
    ActivityResultLauncher<Intent> image_pick_launcher;
    StorageReference storageReference;
    FirebaseUser user;
    FirebaseAuth f_auth;
    FirebaseFirestore f_store;
    UserModel currentUserModel;
    TextView rating, rank, level, wins, loses;
    ImageView profile_picture;
    static String userID;
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

        // getUserData();

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

        // Assuming you have obtained user data after registration
        String userEmail = user.getEmail();

        // Fetch and set the user's username from Firestorm
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

        // Fetch and set the user's ID from Firestorm
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
            ImagePicker.with(this).cropSquare().compress(518).maxResultSize(518, 518)
                    .createIntent(intent -> {
                        image_pick_launcher.launch(intent);
                        return null;
                    });
        });

        save_changes.setOnClickListener(v -> {
            /*if (selected_image_uri != null)
                getCurrentProfilePictureStorageRef().putFile(selected_image_uri)
                        .addOnCompleteListener(task -> {
                            updateToFirebaseStorage();
                        });
            else
                updateToFirebaseStorage();*/
        });

    }

    private static void setProfilePicture(Context context, Uri imageUri, ImageView imageView) {
        Glide.with(context).load(imageUri).into(imageView);
    }

    @NonNull
    private static StorageReference getCurrentProfilePictureStorageRef() {
        return FirebaseStorage.getInstance().getReference().child("profile_pictures")
                .child(userID).child("profile_picture.jpg");
    }

    private void updateToFirebaseStorage() {
        currentUserDetails().set(currentUserModel).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                Toast.makeText(this, "Profile image updated successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Failed to update profile image", Toast.LENGTH_SHORT).show();
        });
    }

    void getUserData() {
        currentUserDetails().get().addOnCompleteListener(task -> {
            currentUserModel = task.getResult().toObject(UserModel.class);

        });
    }


    @NonNull
    public static DocumentReference currentUserDetails() {
        return FirebaseFirestore.getInstance().collection("all my users").document(userID);
    }
}




