package com.example.poisonousking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
// import androidx.fragment.app.Fragment;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class ProfileActivity extends AppCompatActivity {

    SwitchMaterial sound_switch, music_switch;
    Button game_rules, log_out, change_color, delete_account, close;
    TextView privacy_policy, terms_and_conditions;
    FirebaseAuth auth;
    Button play_button_1, play_button_2, play_button_3, menu_button, add_poison_coins;
    ImageView your_profile_picture;
    TextView your_username;
    FirebaseUser user;
    Dialog logout_dialog, dialog_profile_menu, quick_game_dialog, classic_game_dialog, big_game_dialog;
    Button quick_game_close, classic_game_close, big_game_close;
    Button about_quick_game, about_classic_game, about_big_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseFirestore f_store = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        your_profile_picture = findViewById(R.id.your_profile_picture);
        your_username = findViewById(R.id.your_username);
        play_button_1 = findViewById(R.id.play_button);
        play_button_2 = findViewById(R.id.play_button_2);
        play_button_3 = findViewById(R.id.play_button_3);
        add_poison_coins = findViewById(R.id.coins_getting_plus);
        about_quick_game = findViewById(R.id.about_quick_game);
        about_classic_game = findViewById(R.id.about_classic_game);
        about_big_game = findViewById(R.id.about_big_game);
        menu_button = findViewById(R.id.menu_button);
        user = auth.getCurrentUser();

        play_button_1.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, GameFieldActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Your game will start soon. Good luck!", Toast.LENGTH_SHORT).show();
        });

        play_button_2.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, GameFieldActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Your game will start soon. Good luck!", Toast.LENGTH_SHORT).show();
        });

        play_button_3.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, GameFieldActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Your game will start soon. Good luck!", Toast.LENGTH_SHORT).show();
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

        // All necessary attributes for Menu Dialog
        dialog_profile_menu = new Dialog(ProfileActivity.this);
        dialog_profile_menu.setContentView(R.layout.dialog_profile_menu);
        Objects.requireNonNull(dialog_profile_menu.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_profile_menu.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        dialog_profile_menu.setCancelable(false);

        sound_switch = dialog_profile_menu.findViewById(R.id.sound_switch);
        music_switch = dialog_profile_menu.findViewById(R.id.music_switch);
        game_rules = dialog_profile_menu.findViewById(R.id.game_rules_button);
        log_out = dialog_profile_menu.findViewById(R.id.logout);
        change_color = dialog_profile_menu.findViewById(R.id.change_color_button);
        delete_account = dialog_profile_menu.findViewById(R.id.delete_account_button);
        close = dialog_profile_menu.findViewById(R.id.close_button);
        privacy_policy = dialog_profile_menu.findViewById(R.id.privacy_policy);
        terms_and_conditions = dialog_profile_menu.findViewById(R.id.terms_and_conditions);

        delete_account.setOnClickListener(v -> {
            deleteAccountForever();
        });

        Window menu_window = dialog_profile_menu.getWindow();
        if (menu_window != null) {
            menu_window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            menu_window.setGravity(Gravity.CENTER); // Set the gravity to top
            menu_window.setWindowAnimations(R.style.DialogAnimation); // Set the animation
        }

        menu_button.setOnClickListener(v -> {
            dialog_profile_menu.show();
        });

        close.setOnClickListener(v -> {
            dialog_profile_menu.dismiss();
        });

        log_out.setOnClickListener(v -> {
            onLogOutButtonClick();
        });

        // All necessary attributes for Quick Game Dialog
        quick_game_dialog = new Dialog(ProfileActivity.this);
        quick_game_dialog.setContentView(R.layout.dialog_about_quick_game);
        Objects.requireNonNull(quick_game_dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        quick_game_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        quick_game_dialog.setCancelable(false);

        quick_game_close = quick_game_dialog.findViewById(R.id.quick_game_close_button);

        Window quick_game_window = quick_game_dialog.getWindow();
        if (quick_game_window != null) {
            quick_game_window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            quick_game_window.setGravity(Gravity.CENTER); // Set the gravity to top
            quick_game_window.setWindowAnimations(R.style.DialogAnimation); // Set the animation
        }

        about_quick_game.setOnClickListener(v -> {
            quick_game_dialog.show();
        });

        quick_game_close.setOnClickListener(v -> {
            quick_game_dialog.dismiss();
        });

        // All necessary attributes for Classic Game Dialog
        classic_game_dialog = new Dialog(ProfileActivity.this);
        classic_game_dialog.setContentView(R.layout.dialog_about_classic_game);
        Objects.requireNonNull(classic_game_dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        classic_game_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        classic_game_dialog.setCancelable(false);

        classic_game_close = classic_game_dialog.findViewById(R.id.classic_game_close_button);

        Window classic_game_window = classic_game_dialog.getWindow();
        if (classic_game_window != null) {
            classic_game_window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            classic_game_window.setGravity(Gravity.CENTER); // Set the gravity to top
            classic_game_window.setWindowAnimations(R.style.DialogAnimation); // Set the animation
        }

        about_classic_game.setOnClickListener(v -> {
            classic_game_dialog.show();
        });

        classic_game_close.setOnClickListener(v -> {
            classic_game_dialog.dismiss();
        });

        // All necessary attributes for Big Game Dialog
        big_game_dialog = new Dialog(ProfileActivity.this);
        big_game_dialog.setContentView(R.layout.dialog_about_big_game);
        Objects.requireNonNull(big_game_dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        big_game_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        big_game_dialog.setCancelable(false);

        big_game_close = big_game_dialog.findViewById(R.id.big_game_close_button);

        Window big_game_window = big_game_dialog.getWindow();
        if (big_game_window != null) {
            big_game_window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            big_game_window.setGravity(Gravity.CENTER); // Set the gravity to top
            big_game_window.setWindowAnimations(R.style.DialogAnimation); // Set the animation
        }

        about_big_game.setOnClickListener(v -> {
            big_game_dialog.show();
        });

        big_game_close.setOnClickListener(v -> {
            big_game_dialog.dismiss();
        });
    }

    private void deleteAccountForever() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // Delete the user's account
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Account deletion successful
                            Toast.makeText(ProfileActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                            // Here you can navigate to another activity or perform any other actions
                        } else {
                            // Account deletion failed
                            Toast.makeText(ProfileActivity.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void onLogOutButtonClick() {
        // logout_dialog.show();
        auth.signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        dialog_profile_menu.dismiss();
    }
}