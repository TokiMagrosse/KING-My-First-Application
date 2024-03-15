package com.example.poisonousking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameFieldActivity extends AppCompatActivity {

    Button back_to_profile;
    private ImageView[] image_views;
    private final int[] card_images = {
            R.drawable.ace_of_clubs, R.drawable.ace_of_diamonds, R.drawable.ace_of_hearts, R.drawable.ace_of_spades,
            R.drawable.king_of_clubs, R.drawable.king_of_diamonds, R.drawable.ace_of_hearts, R.drawable.king_of_spades,
            R.drawable.queen_of_clubs, R.drawable.queen_of_diamonds, R.drawable.queen_of_hearts, R.drawable.queen_of_spades,
            R.drawable.jack_of_clubs, R.drawable.jack_of_diamonds, R.drawable.jack_of_hearts, R.drawable.jack_of_spades,
            R.drawable.ten_of_clubs, R.drawable.ten_of_diamonds, R.drawable.ten_of_hearts, R.drawable.ten_of_spades,
            R.drawable.nine_of_clubs, R.drawable.ten_of_diamonds, R.drawable.ten_of_hearts, R.drawable.ten_of_spades,
            R.drawable.eight_of_clubs, R.drawable.eight_of_diamonds, R.drawable.eight_of_hearts, R.drawable.eight_of_spades,
            R.drawable.seven_of_clubs, R.drawable.seven_of_diamonds, R.drawable.seven_of_hearts, R.drawable.seven_of_spades
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_field);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        image_views = new ImageView[8];
        image_views[0] = findViewById(R.id.my_card_1);
        image_views[1] = findViewById(R.id.my_card_2);
        image_views[2] = findViewById(R.id.my_card_3);
        image_views[3] = findViewById(R.id.my_card_4);
        image_views[4] = findViewById(R.id.my_card_5);
        image_views[5] = findViewById(R.id.my_card_6);
        image_views[6] = findViewById(R.id.my_card_7);
        image_views[7] = findViewById(R.id.my_card_8);

        distributeRandomCards();

        back_to_profile = findViewById(R.id.back_to_profile);
        back_to_profile.setOnClickListener(v -> {
            Intent intent = new Intent(GameFieldActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

    }

    private void distributeRandomCards() {
        List<Integer> deck = generateDeckOfCards();
        Collections.shuffle(deck);

        // Assign the first 8 cards to the ImageViews
        for (int i = 0; i < 8; i++) {
            image_views[i].setImageResource(deck.get(i));
        }
    }

    private List<Integer> generateDeckOfCards() {
        List<Integer> deck = new ArrayList<>();
        // Add each card image resource ID to the deck
        for (int image_id : card_images) {
            deck.add(image_id);
        }
        return deck;
    }

}