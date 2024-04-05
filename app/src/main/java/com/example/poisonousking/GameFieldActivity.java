package com.example.poisonousking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameFieldActivity extends AppCompatActivity {

    CardView card_door_1, card_door_2, card_door_3, card_door_4;
    CardView card_door_5, card_door_6, card_door_7, card_door_8;
    ImageView center_card_1, center_card_2, center_card_3, center_card_4;
    Button back_to_profile;
    private ImageView[] image_views;
    private final int[] card_images = {
            R.drawable.seven_of_clubs, R.drawable.seven_of_diamonds, R.drawable.seven_of_hearts, R.drawable.seven_of_spades,
            R.drawable.eight_of_clubs, R.drawable.eight_of_diamonds, R.drawable.eight_of_hearts, R.drawable.eight_of_spades,
            R.drawable.nine_of_clubs, R.drawable.nine_of_diamonds, R.drawable.nine_of_hearts, R.drawable.nine_of_spades,
            R.drawable.ten_of_clubs, R.drawable.ten_of_diamonds, R.drawable.ten_of_hearts, R.drawable.ten_of_spades,
            R.drawable.jack_of_clubs, R.drawable.jack_of_diamonds, R.drawable.jack_of_hearts, R.drawable.jack_of_spades,
            R.drawable.queen_of_clubs, R.drawable.queen_of_diamonds, R.drawable.queen_of_hearts, R.drawable.queen_of_spades,
            R.drawable.king_of_clubs, R.drawable.king_of_diamonds, R.drawable.ace_of_hearts, R.drawable.king_of_spades,
            R.drawable.ace_of_clubs, R.drawable.ace_of_diamonds, R.drawable.ace_of_hearts, R.drawable.ace_of_spades
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

        // Card doors that will block players card(s) changing his color
        // green - means you can throw it, red - means you can't throw it
        card_door_1 = findViewById(R.id.card_door_1);
        card_door_2 = findViewById(R.id.card_door_2);
        card_door_3 = findViewById(R.id.card_door_3);
        card_door_4 = findViewById(R.id.card_door_4);
        card_door_5 = findViewById(R.id.card_door_5);
        card_door_6 = findViewById(R.id.card_door_6);
        card_door_7 = findViewById(R.id.card_door_7);
        card_door_8 = findViewById(R.id.card_door_8);

        // Four cells for the cards in the center
        center_card_1 = findViewById(R.id.center_card_1);
        center_card_2 = findViewById(R.id.center_card_2);
        center_card_3 = findViewById(R.id.center_card_3);
        center_card_4 = findViewById(R.id.center_card_4);

        image_views = new ImageView[8];
        image_views[0] = findViewById(R.id.my_card_1);
        image_views[1] = findViewById(R.id.my_card_2);
        image_views[2] = findViewById(R.id.my_card_3);
        image_views[3] = findViewById(R.id.my_card_4);
        image_views[4] = findViewById(R.id.my_card_5);
        image_views[5] = findViewById(R.id.my_card_6);
        image_views[6] = findViewById(R.id.my_card_7);
        image_views[7] = findViewById(R.id.my_card_8);

        // distributeRandomCards();
        List<Integer> deck = generateDeckOfCards();
        Collections.shuffle(deck);

        // Divide the deck into 4 lists, each representing the cards for one player
        List<Integer> users_cards = deck.subList(0, 8);
        List<Integer> first_bot_cards = deck.subList(8, 16);
        List<Integer> second_bot_cards = deck.subList(16, 24);
        List<Integer> third_bot_cards = deck.subList(24, 32);

        // Assign the cards to the ImageViews for the first player
        for (int i = 0; i < 8; i++) {
            image_views[i].setImageResource(users_cards.get(i));
        }

        // Lists of each suit sorted by increasing of card values
        List<Integer> CLUBS = allIDesOfClubsSorted();
        List<Integer> DIAMONDS = allIDesOfDiamondsSorted();
        List<Integer> HEARTS = allIDesOfHeartsSorted();
        List<Integer> SPADES = allIDesOfSpadesSorted();



        // Button that brings user into the main page of the game
        back_to_profile = findViewById(R.id.back_to_profile);
        back_to_profile.setOnClickListener(v -> {
            Toast.makeText(this, "You left the game", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(GameFieldActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Logic of throwing the card from bottom to center
        image_views[0].setOnClickListener(v -> {
            center_card_1.setImageDrawable(image_views[0].getDrawable());
            center_card_1.setVisibility(View.VISIBLE);
            image_views[0].setVisibility(View.GONE);
            card_door_1.setVisibility(View.GONE);
        });
        image_views[1].setOnClickListener(v -> {
            center_card_1.setImageDrawable(image_views[1].getDrawable());
            center_card_1.setVisibility(View.VISIBLE);
            image_views[1].setVisibility(View.GONE);
            card_door_2.setVisibility(View.GONE);
        });
        image_views[2].setOnClickListener(v -> {
            center_card_1.setImageDrawable(image_views[2].getDrawable());
            center_card_1.setVisibility(View.VISIBLE);
            image_views[2].setVisibility(View.GONE);
            card_door_3.setVisibility(View.GONE);
        });
        image_views[3].setOnClickListener(v -> {
            center_card_1.setImageDrawable(image_views[3].getDrawable());
            center_card_1.setVisibility(View.VISIBLE);
            image_views[3].setVisibility(View.GONE);
            card_door_4.setVisibility(View.GONE);
        });
        image_views[4].setOnClickListener(v -> {
            center_card_1.setImageDrawable(image_views[4].getDrawable());
            center_card_1.setVisibility(View.VISIBLE);
            image_views[4].setVisibility(View.GONE);
            card_door_5.setVisibility(View.GONE);
        });
        image_views[5].setOnClickListener(v -> {
            center_card_1.setImageDrawable(image_views[5].getDrawable());
            center_card_1.setVisibility(View.VISIBLE);
            image_views[5].setVisibility(View.GONE);
            card_door_6.setVisibility(View.GONE);
        });
        image_views[6].setOnClickListener(v -> {
            center_card_1.setImageDrawable(image_views[6].getDrawable());
            center_card_1.setVisibility(View.VISIBLE);
            image_views[6].setVisibility(View.GONE);
            card_door_7.setVisibility(View.GONE);
        });
        image_views[7].setOnClickListener(v -> {
            center_card_1.setImageDrawable(image_views[7].getDrawable());
            center_card_1.setVisibility(View.VISIBLE);
            image_views[7].setVisibility(View.GONE);
            card_door_8.setVisibility(View.GONE);
        });

    }

    private List<Integer> generateDeckOfCards() {
        List<Integer> deck = new ArrayList<>();
        // Add each card image resource ID to the deck
        for (int image_id : card_images) {
            deck.add(image_id);
        }
        return deck;
    }

    private List<Integer> allIDesOfClubsSorted() {
        List<Integer> clubs = new ArrayList<>();
        for (int i = 0; i < card_images.length; i += 4) {
            clubs.add(card_images[i]);
        }
        return clubs;
    }

    private List<Integer> allIDesOfDiamondsSorted() {
        List<Integer> diamonds = new ArrayList<>();
        for (int i = 1; i < card_images.length; i += 4) {
            diamonds.add(card_images[i]);
        }
        return diamonds;
    }

    private List<Integer> allIDesOfHeartsSorted() {
        List<Integer> hearts = new ArrayList<>();
        for (int i = 2; i < card_images.length; i += 4) {
            hearts.add(card_images[i]);
        }
        return hearts;
    }

    private List<Integer> allIDesOfSpadesSorted() {
        List<Integer> spades = new ArrayList<>();
        for (int i = 3; i < card_images.length; i += 4) {
            spades.add(card_images[i]);
        }
        return spades;
    }

}
