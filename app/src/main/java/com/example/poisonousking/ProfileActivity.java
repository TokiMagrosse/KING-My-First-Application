package com.example.poisonousking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
// import androidx.fragment.app.Fragment;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    Button play_button, menu_button, logout_finally;
    ImageView your_profile_picture;
    TextView your_username;
    FirebaseUser user;
    Dialog logout_dialog, menu_dialog;
    Button cancel_button, logout_button;

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
        your_profile_picture = findViewById(R.id.your_profile_picture);
        your_username = findViewById(R.id.your_username);
        logout_finally = findViewById(R.id.play_button_3);
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

        menu_dialog = new Dialog(ProfileActivity.this);
        menu_dialog.setContentView(R.layout.profile_menu_dialog);
        Objects.requireNonNull(menu_dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(logout_dialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.logout_dialog_bg));
        logout_dialog.setCancelable(false);

        sound_switch = menu_dialog.findViewById(R.id.sound_switch);
        music_switch = menu_dialog.findViewById(R.id.music_switch);
        game_rules = menu_dialog.findViewById(R.id.game_rules_button);
        log_out = menu_dialog.findViewById(R.id.logout);
        change_color = menu_dialog.findViewById(R.id.change_color_button);
        delete_account = menu_dialog.findViewById(R.id.delete_account_button);
        close = menu_dialog.findViewById(R.id.close_button);
        privacy_policy = menu_dialog.findViewById(R.id.privacy_policy);
        terms_and_conditions = menu_dialog.findViewById(R.id.terms_and_conditions);

        menu_button.setOnClickListener(v -> {
            menu_dialog.show();
        });

        close.setOnClickListener(v -> {
            menu_dialog.dismiss();
        });

        logout_dialog = new Dialog(ProfileActivity.this);
        logout_dialog.setContentView(R.layout.log_out_dialog_box);
        Objects.requireNonNull(logout_dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        logout_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.logout_dialog_bg));
        logout_dialog.setCancelable(false);

        cancel_button = logout_dialog.findViewById(R.id.cancel_button);
        logout_button = logout_dialog.findViewById(R.id.yes_button);

        cancel_button.setOnClickListener(v -> {
            logout_dialog.dismiss();
        });

        logout_button.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            logout_dialog.dismiss();
        });

        logout_finally.setOnClickListener(v -> {
            logout_dialog.show();
        });

    }
}