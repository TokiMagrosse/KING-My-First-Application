package com.example.poisonousking.inside_of_king;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.poisonousking.R;
import com.example.poisonousking.helper_classes.MusicService;
import com.example.poisonousking.outside_of_king.LogInActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    public int gold_coins_count, user_rating_number;
    FirebaseFirestore f_store;
    // Define the volume level you want (0.0 - 1.0 range)
    MediaPlayer buttonClickSound;
    private static final float BACKGROUND_MUSIC_VOLUME = 0.35f; // Set volume level to 35% for background music
    SwitchMaterial sound_switch, music_switch;
    Button game_rules, log_out, language, delete_account, close;
    TextView privacy_policy, terms_and_conditions;
    FirebaseAuth auth;
    Button play_button_1, play_button_2, play_button_3, menu_button, add_poison_coins;
    ImageView your_profile_picture;
    TextView your_username, gold_coins, my_rating;
    FirebaseUser user;
    static String userID;
    Dialog dialog_profile_menu, quick_game_dialog, classic_game_dialog, big_game_dialog, log_out_dialog, delete_account_dialog;
    Button quick_game_close, classic_game_close, big_game_close, yes_log_out, cancel_log_out;
    Button about_quick_game, about_classic_game, about_big_game, delete_forever, cancel_deletion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase
        f_store = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        your_profile_picture = findViewById(R.id.your_profile_picture);
        your_username = findViewById(R.id.your_username);
        gold_coins = findViewById(R.id.gold_coins_count);
        my_rating = findViewById(R.id.my_rating);
        play_button_1 = findViewById(R.id.play_button_1);
        play_button_2 = findViewById(R.id.play_button_2);
        play_button_3 = findViewById(R.id.play_button_3);
        add_poison_coins = findViewById(R.id.coins_getting_plus);
        about_quick_game = findViewById(R.id.about_quick_game);
        about_classic_game = findViewById(R.id.about_classic_game);
        about_big_game = findViewById(R.id.about_big_game);
        menu_button = findViewById(R.id.menu_button);
        user = auth.getCurrentUser();

        getUserStats(userID);

        buttonClickSound = MediaPlayer.create(this, R.raw.button_click_sound_1);
        // Set the volume of the mediaPlayer to a lower level (background music volume)
        buttonClickSound.setVolume(BACKGROUND_MUSIC_VOLUME, BACKGROUND_MUSIC_VOLUME);

        add_poison_coins.setOnClickListener(v -> {
            if (gold_coins_count <= 500) getHundredCoinsForFree(userID);
            else Toast.makeText(this, "You have enough coins to play", Toast.LENGTH_SHORT).show();
        });

        Intent musicIntent = new Intent(this, MusicService.class);
        startService(musicIntent);

        play_button_1.setOnClickListener(v -> {
            buttonClickSound.start();
            Intent intentTwo = new Intent(this, MusicService.class);
            stopService(intentTwo);
            if (gold_coins_count < 100)
                Toast.makeText(this, "You don't have enough coins", Toast.LENGTH_SHORT).show();
            if (gold_coins_count >= 100) {
                userBet(userID);
                Intent intent = new Intent(HomeActivity.this, QuickGameFieldActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Your game will start soon. Good luck!", Toast.LENGTH_SHORT).show();
            }
        });

        play_button_2.setOnClickListener(v -> {
            buttonClickSound.start();
            Toast.makeText(this, "Sorry! I'm still working on this", Toast.LENGTH_SHORT).show();
        });

        play_button_3.setOnClickListener(v -> {
            buttonClickSound.start();
            Toast.makeText(this, "Sorry! I'm still working on this", Toast.LENGTH_SHORT).show();
        });

        your_profile_picture.setOnClickListener(v -> {
            buttonClickSound.start();
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
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

        fetchAndDisplayProfileImage();

        // All necessary attributes for Menu Dialog
        dialog_profile_menu = new Dialog(HomeActivity.this);
        dialog_profile_menu.setContentView(R.layout.dialog_profile_menu);
        Objects.requireNonNull(dialog_profile_menu.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_profile_menu.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_dialog_bg));
        dialog_profile_menu.setCancelable(false);
        sound_switch = dialog_profile_menu.findViewById(R.id.sound_switch);
        sound_switch.setThumbTintList(ContextCompat.getColorStateList(this, R.color.fucking_green));
        sound_switch.setTrackTintList(ContextCompat.getColorStateList(this, R.color.green_2));

        // Add a listener to the sound switch
        sound_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // buttonClickSound.reset();
                // If the switch is on, start the music
                sound_switch.setThumbTintList(ContextCompat.getColorStateList(this, R.color.fucking_green));
                sound_switch.setTrackTintList(ContextCompat.getColorStateList(this, R.color.green_2));
            } else {
                buttonClickSound.stop();
                // If the switch is off, stop the music
                sound_switch.setThumbTintList(ContextCompat.getColorStateList(this, R.color.black));
                sound_switch.setTrackTintList(ContextCompat.getColorStateList(this, R.color.grey_4));
            }
        });

        music_switch = dialog_profile_menu.findViewById(R.id.music_switch);
        music_switch.setThumbTintList(ContextCompat.getColorStateList(this, R.color.fucking_green));
        music_switch.setTrackTintList(ContextCompat.getColorStateList(this, R.color.green_2));

        // Add a listener to the music switch
        music_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Intent intent = new Intent(HomeActivity.this, MusicService.class);
            if (isChecked) {
                // If the switch is on, start the music
                music_switch.setThumbTintList(ContextCompat.getColorStateList(this, R.color.fucking_green));
                music_switch.setTrackTintList(ContextCompat.getColorStateList(this, R.color.green_2));

            } else {
                // If the switch is off, stop the music
                music_switch.setThumbTintList(ContextCompat.getColorStateList(this, R.color.black));
                music_switch.setTrackTintList(ContextCompat.getColorStateList(this, R.color.grey_4));

            }
        });

        game_rules = dialog_profile_menu.findViewById(R.id.game_rules_button);
        game_rules.setOnClickListener(v -> {
            buttonClickSound.start();
            Toast.makeText(this, "Haven't drown the rules yet", Toast.LENGTH_SHORT).show();
            /*Intent intent = new Intent(this, GameRulesActivity.class);
            startActivity(intent);*/
        });
        log_out = dialog_profile_menu.findViewById(R.id.logout);

        // All necessary attributes for Log out Dialog
        log_out_dialog = new Dialog(HomeActivity.this);
        log_out_dialog.setContentView(R.layout.dialog_log_out_warning);
        Objects.requireNonNull(log_out_dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        log_out_dialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_dialog_bg));
        log_out_dialog.setCancelable(false);

        Window log_out_window = log_out_dialog.getWindow();
        if (log_out_window != null) {
            log_out_window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            log_out_window.setGravity(Gravity.CENTER); // Set the gravity to top
        }

        yes_log_out = log_out_dialog.findViewById(R.id.yes_button);
        cancel_log_out = log_out_dialog.findViewById(R.id.cancel_button);

        log_out.setOnClickListener(v -> {
            buttonClickSound.start();
            log_out_dialog.show();
        });
        yes_log_out.setOnClickListener(v -> {
            buttonClickSound.start();
            onLogOutButtonClick();
        });
        cancel_log_out.setOnClickListener(v -> {
            buttonClickSound.start();
            log_out_dialog.dismiss();
        });

        language = dialog_profile_menu.findViewById(R.id.change_color_button);
        delete_account = dialog_profile_menu.findViewById(R.id.delete_account_button);

        // All necessary attributes for Delete account Dialog
        delete_account_dialog = new Dialog(HomeActivity.this);
        delete_account_dialog.setContentView(R.layout.dialog_delete_account_warning);
        Objects.requireNonNull(delete_account_dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        delete_account_dialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_dialog_bg));
        delete_account_dialog.setCancelable(false);

        Window delete_window = delete_account_dialog.getWindow();
        if (delete_window != null) {
            delete_window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            delete_window.setGravity(Gravity.CENTER); // Set the gravity to top
        }

        language.setOnClickListener(v -> {
            buttonClickSound.start();
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            dialog_profile_menu.dismiss();
        });

        delete_account.setOnClickListener(v -> {
            buttonClickSound.start();
            delete_account_dialog.show();
        });
        delete_forever = delete_account_dialog.findViewById(R.id.delete_forever);
        cancel_deletion = delete_account_dialog.findViewById(R.id.cancel_deletion);

        delete_forever.setOnClickListener(v -> {
            buttonClickSound.start();
            deleteAccountForever();
            onLogOutButtonClick();
        });
        cancel_deletion.setOnClickListener(v -> {
            buttonClickSound.start();
            delete_account_dialog.dismiss();
        });

        close = dialog_profile_menu.findViewById(R.id.close_button);
        privacy_policy = dialog_profile_menu.findViewById(R.id.privacy_policy);
        terms_and_conditions = dialog_profile_menu.findViewById(R.id.terms_and_conditions);

        Window menu_window = dialog_profile_menu.getWindow();
        if (menu_window != null) {
            menu_window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            menu_window.setGravity(Gravity.CENTER); // Set the gravity to top
            menu_window.setWindowAnimations(R.style.DialogAnimation); // Set the animation
        }

        menu_button.setOnClickListener(v -> {
            buttonClickSound.start();
            dialog_profile_menu.show();
        });
        close.setOnClickListener(v -> {
            buttonClickSound.start();
            dialog_profile_menu.dismiss();
        });

        // All necessary attributes for Quick Game Dialog
        quick_game_dialog = new Dialog(HomeActivity.this);
        quick_game_dialog.setContentView(R.layout.dialog_about_quick_game);
        Objects.requireNonNull(quick_game_dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        quick_game_dialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_dialog_bg));
        quick_game_dialog.setCancelable(false);

        quick_game_close = quick_game_dialog.findViewById(R.id.quick_game_close_button);

        Window quick_game_window = quick_game_dialog.getWindow();
        if (quick_game_window != null) {
            quick_game_window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            quick_game_window.setGravity(Gravity.CENTER); // Set the gravity to top
            quick_game_window.setWindowAnimations(R.style.DialogAnimation); // Set the animation
        }

        about_quick_game.setOnClickListener(v -> {
            buttonClickSound.start();
            quick_game_dialog.show();
        });
        quick_game_close.setOnClickListener(v -> {
            buttonClickSound.start();
            quick_game_dialog.dismiss();
        });

        // All necessary attributes for Classic Game Dialog
        classic_game_dialog = new Dialog(HomeActivity.this);
        classic_game_dialog.setContentView(R.layout.dialog_about_classic_game);
        Objects.requireNonNull(classic_game_dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        classic_game_dialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_dialog_bg));
        classic_game_dialog.setCancelable(false);

        classic_game_close = classic_game_dialog.findViewById(R.id.classic_game_close_button);

        Window classic_game_window = classic_game_dialog.getWindow();
        if (classic_game_window != null) {
            classic_game_window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            classic_game_window.setGravity(Gravity.CENTER); // Set the gravity to top
            classic_game_window.setWindowAnimations(R.style.DialogAnimation); // Set the animation
        }

        about_classic_game.setOnClickListener(v -> {
            buttonClickSound.start();
            classic_game_dialog.show();
        });
        classic_game_close.setOnClickListener(v -> {
            buttonClickSound.start();
            classic_game_dialog.dismiss();
        });

        // All necessary attributes for Big Game Dialog
        big_game_dialog = new Dialog(HomeActivity.this);
        big_game_dialog.setContentView(R.layout.dialog_about_big_game);
        Objects.requireNonNull(big_game_dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        big_game_dialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_dialog_bg));
        big_game_dialog.setCancelable(false);

        big_game_close = big_game_dialog.findViewById(R.id.big_game_close_button);

        Window big_game_window = big_game_dialog.getWindow();
        if (big_game_window != null) {
            big_game_window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            big_game_window.setGravity(Gravity.CENTER); // Set the gravity to top
            big_game_window.setWindowAnimations(R.style.DialogAnimation); // Set the animation
        }

        about_big_game.setOnClickListener(v -> {
            buttonClickSound.start();
            big_game_dialog.show();
        });
        big_game_close.setOnClickListener(v -> {
            buttonClickSound.start();
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
                            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            // Account deletion successful
                            Toast.makeText(HomeActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                            // Here you can navigate to another activity or perform any other actions
                        } else {
                            // Account deletion failed
                            Toast.makeText(HomeActivity.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void onLogOutButtonClick() {
        auth.signOut();
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        dialog_profile_menu.dismiss();
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
                            .into(your_profile_picture);
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
                    user_rating_number = Objects.requireNonNull(document.getLong("Rating")).intValue();
                    gold_coins_count = Objects.requireNonNull(document.getLong("Coins")).intValue();

                    // Update the TextViews with the fetched data on the main thread
                    runOnUiThread(() -> {
                        my_rating.setText(String.valueOf(user_rating_number));
                        gold_coins.setText(String.valueOf(gold_coins_count));
                    });

                } else {
                    runOnUiThread(() -> Toast.makeText(this, "No such document", Toast.LENGTH_SHORT).show());
                }
            } else {
                Log.d("HomeActivity", "get failed with ", task.getException());
            }
        });
    }

    private void userBet(String documentId) {
        gold_coins_count -= 100;
        Map<String, Object> updates = new HashMap<>();
        updates.put("Coins", gold_coins_count);

        f_store.collection("all my users").document(documentId).update(updates)
                .addOnSuccessListener(aVoid -> Log.d("QuickGameActivity", "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w("QuickGameActivity", "Error updating document", e));
    }

    // Method to update game counts when a user wins
    private void getHundredCoinsForFree(String documentId) {
        gold_coins_count += 100;

        Map<String, Object> updates = new HashMap<>();
        updates.put("Coins", gold_coins_count);

        f_store.collection("all my users").document(documentId).update(updates)
                .addOnSuccessListener(aVoid -> Log.d("HomeActivity", "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w("HomeActivity", "Error updating document", e));
    }
}