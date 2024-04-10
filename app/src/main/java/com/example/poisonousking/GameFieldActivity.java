package com.example.poisonousking;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameFieldActivity extends AppCompatActivity {

    protected String[] game_modes = {"NO TRICKS", "NO LAST 2", "NO JACKS", "NO POISONOUS KING", "TAKE TRICKS"};
    TextView imageID;
    TextView user_score, first_bot_score, second_bot_score, third_bot_score;
    private static final int[] user_card_doors_IDes = {
            R.id.card_door_1, R.id.card_door_2, R.id.card_door_3, R.id.card_door_4,
            R.id.card_door_5, R.id.card_door_6, R.id.card_door_7, R.id.card_door_8
    };
    private final CardView[] user_card_door_views = new CardView[8];
    ImageView[] four_center_cell_views = new ImageView[4];
    private final ImageView[] user_card_image_views = new ImageView[8];
    Button table_button, menu_button;
    private final int[] card_images = {
            R.drawable.seven_of_clubs, R.drawable.seven_of_diamonds, R.drawable.seven_of_hearts, R.drawable.seven_of_spades,
            R.drawable.eight_of_clubs, R.drawable.eight_of_diamonds, R.drawable.eight_of_hearts, R.drawable.eight_of_spades,
            R.drawable.nine_of_clubs, R.drawable.nine_of_diamonds, R.drawable.nine_of_hearts, R.drawable.nine_of_spades,
            R.drawable.ten_of_clubs, R.drawable.ten_of_diamonds, R.drawable.ten_of_hearts, R.drawable.ten_of_spades,
            R.drawable.jack_of_clubs, R.drawable.jack_of_diamonds, R.drawable.jack_of_hearts, R.drawable.jack_of_spades,
            R.drawable.queen_of_clubs, R.drawable.queen_of_diamonds, R.drawable.queen_of_hearts, R.drawable.queen_of_spades,
            R.drawable.king_of_clubs, R.drawable.king_of_diamonds, R.drawable.king_of_hearts, R.drawable.king_of_spades,
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

        imageID = findViewById(R.id.image_ID);
        user_score = findViewById(R.id.user_score);
        first_bot_score = findViewById(R.id.first_bot_score);
        second_bot_score = findViewById(R.id.second_bot_score);
        third_bot_score = findViewById(R.id.third_bot_score);
        table_button = findViewById(R.id.table_button);
        menu_button = findViewById(R.id.menu_button);

        // Initializing arrays after setContentView
        user_card_door_views[0] = findViewById(R.id.card_door_1);
        user_card_door_views[1] = findViewById(R.id.card_door_2);
        user_card_door_views[2] = findViewById(R.id.card_door_3);
        user_card_door_views[3] = findViewById(R.id.card_door_4);
        user_card_door_views[4] = findViewById(R.id.card_door_5);
        user_card_door_views[5] = findViewById(R.id.card_door_6);
        user_card_door_views[6] = findViewById(R.id.card_door_7);
        user_card_door_views[7] = findViewById(R.id.card_door_8);

        four_center_cell_views[0] = findViewById(R.id.center_card_1);
        four_center_cell_views[1] = findViewById(R.id.center_card_2);
        four_center_cell_views[2] = findViewById(R.id.center_card_3);
        four_center_cell_views[3] = findViewById(R.id.center_card_4);

        user_card_image_views[0] = findViewById(R.id.my_card_1);
        user_card_image_views[1] = findViewById(R.id.my_card_2);
        user_card_image_views[2] = findViewById(R.id.my_card_3);
        user_card_image_views[3] = findViewById(R.id.my_card_4);
        user_card_image_views[4] = findViewById(R.id.my_card_5);
        user_card_image_views[5] = findViewById(R.id.my_card_6);
        user_card_image_views[6] = findViewById(R.id.my_card_7);
        user_card_image_views[7] = findViewById(R.id.my_card_8);

        // Function to shuffle the 32-card deck (3 times) and distribute to 4 players by random
        List<Integer> deck = generateDeckOfCards();
        Collections.shuffle(deck);
        Collections.shuffle(deck);
        Collections.shuffle(deck);

        // 4 arrays of card IDes sorted by their values
        List<Integer> CLUBS = sortedClubs();
        List<Integer> DIAMONDS = sortedDiamonds();
        List<Integer> HEARTS = sortedHearts();
        List<Integer> SPADES = sortedSpades();

        // Divide the deck into 4 lists, each representing the cards for one player
        List<Integer> user_cards_IDes = deck.subList(0, 8);
        List<Integer> first_bot_cards_IDes = deck.subList(8, 16);
        List<Integer> second_bot_cards_IDes = deck.subList(16, 24);
        List<Integer> third_bot_cards_IDEs = deck.subList(24, 32);

        /*
         * User cards: 7S, JS, KS, AS, 7C, JC, 7D, 9D
         * P1 cards: 8S, 9C 10C, JD, KD, AD, 9H, QH
         * P2 cards: QC, AC, 10D, QD, 7H, 8, JH, AH
         * P3 cards: 9S, 10S, QS, 8C, KC, 8D, 10H, KH */

        // Creating this arrays with card Ides sorted by suits and valued separately
        List<Integer> cards_sorted_by_suit = allCardsSortedBySuit();
        List<Integer> cards_sorted_by_value = allCardsSortedByValue();

        /*--------------------------Distribution of user cards by random------------------------------*/
        List<Integer> user_sorted_by_suit = currentPlayerCardsSortedBySuit(user_cards_IDes);
        for (int i = 0; i < 8; i++)
            user_card_image_views[i].setImageResource(user_sorted_by_suit.get(i));

        // Dividing first bot cards to 4 parts: sorted spades, clubs, diamonds, hearts
        List<Integer> P1_sorted_by_suit = currentPlayerCardsSortedBySuit(first_bot_cards_IDes);

        // Four lists of IDes separately for each suitable sorted part
        List<Integer> P1_spades = new ArrayList<>();
        List<Integer> P1_clubs = new ArrayList<>();
        List<Integer> P1_diamonds = new ArrayList<>();
        List<Integer> P1_hearts = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            if (SPADES.contains(P1_sorted_by_suit.get(i)))
                P1_spades.add(P1_sorted_by_suit.get(i));
            if (CLUBS.contains(P1_sorted_by_suit.get(i)))
                P1_clubs.add(P1_sorted_by_suit.get(i));
            if (DIAMONDS.contains(P1_sorted_by_suit.get(i)))
                P1_diamonds.add(P1_sorted_by_suit.get(i));
            if (HEARTS.contains(P1_sorted_by_suit.get(i)))
                P1_hearts.add(P1_sorted_by_suit.get(i));
        }

        // Dividing second bot cards to 4 parts: sorted spades, clubs, diamonds, hearts
        List<Integer> P2_sorted_by_suit = currentPlayerCardsSortedBySuit(second_bot_cards_IDes);

        // Four lists of IDes separately for each suitable sorted part
        List<Integer> P2_spades = new ArrayList<>();
        List<Integer> P2_clubs = new ArrayList<>();
        List<Integer> P2_diamonds = new ArrayList<>();
        List<Integer> P2_hearts = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            if (SPADES.contains(P2_sorted_by_suit.get(i)))
                P2_spades.add(P2_sorted_by_suit.get(i));
            if (CLUBS.contains(P2_sorted_by_suit.get(i)))
                P2_clubs.add(P2_sorted_by_suit.get(i));
            if (DIAMONDS.contains(P2_sorted_by_suit.get(i)))
                P2_diamonds.add(P2_sorted_by_suit.get(i));
            if (HEARTS.contains(P2_sorted_by_suit.get(i)))
                P2_hearts.add(P2_sorted_by_suit.get(i));
        }

        // Dividing third bot cards to 4 parts: sorted spades, clubs, diamonds, hearts
        List<Integer> P3_sorted_by_suit = currentPlayerCardsSortedBySuit(third_bot_cards_IDEs);

        // Four lists of IDes separately for each suitable sorted part
        List<Integer> P3_spades = new ArrayList<>();
        List<Integer> P3_clubs = new ArrayList<>();
        List<Integer> P3_diamonds = new ArrayList<>();
        List<Integer> P3_hearts = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            if (SPADES.contains(P3_sorted_by_suit.get(i)))
                P3_spades.add(P3_sorted_by_suit.get(i));
            if (CLUBS.contains(P3_sorted_by_suit.get(i)))
                P3_clubs.add(P3_sorted_by_suit.get(i));
            if (DIAMONDS.contains(P3_sorted_by_suit.get(i)))
                P3_diamonds.add(P3_sorted_by_suit.get(i));
            if (HEARTS.contains(P3_sorted_by_suit.get(i)))
                P3_hearts.add(P3_sorted_by_suit.get(i));
        }

        /*int[] user_valuable_indexes = new int[8];
        for (int i = 0; i < 8; i++) {
            user_valuable_indexes[i] = cards_sorted_by_value.indexOf(user_cards_IDes.get(i));
        }

        Arrays.sort(user_valuable_indexes);
        for (int i = 0; i < 8; i++) {
            user_card_image_views[i].setImageResource(cards_sorted_by_value.get(user_valuable_indexes[i]));
        }*/

        /* The game has already started and it's user's turn first */
        boolean[] cardClickable = new boolean[8];
        Arrays.fill(cardClickable, true); // Initially, all cards are clickable

        int user_current_card_ID;
        for (int i = 0; i < deck.size(); i++) {
            for (byte j = 0; j < 8; j++) {
                if (Objects.equals(user_sorted_by_suit.get(j), cards_sorted_by_value.get(i)) && cardClickable[j]) {
                    user_current_card_ID = user_sorted_by_suit.get(j);
                    user_card_door_views[j].setBackgroundColor(ContextCompat.getColor(this, R.color.fucking_green));
                    byte finalJ = j;
                    user_card_image_views[j].setOnClickListener(v -> {
                        // Set all cards not clickable
                        Arrays.fill(cardClickable, false);

                        // Set the clicked card clickable
                        cardClickable[finalJ] = true;

                        // Display the clicked card in the center
                        four_center_cell_views[0].setImageDrawable(user_card_image_views[finalJ].getDrawable());
                        four_center_cell_views[0].setVisibility(View.VISIBLE);

                        // Hide the clicked card
                        user_card_image_views[finalJ].setVisibility(View.GONE);
                        user_card_door_views[finalJ].setVisibility(View.GONE);

                        // Set all other cards to be not clickable
                        for (int k = 0; k < 8; k++)
                            if (k != finalJ)
                                user_card_image_views[k].setOnClickListener(null);

                    });
                    break;
                }
            }
        }

        // Delay the execution of code for 4000 milliseconds (4 seconds)
        /*if (turn_flag == 1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    four_center_cell_views[1].setImageResource(first_bot_cards_IDes.get(4));
                    four_center_cell_views[1].setVisibility(View.VISIBLE);
                }
            }, 4000); // 4000 milliseconds (4 seconds)
        }*/

    }
    @NonNull
    private List<Integer> currentPlayerCardsSortedBySuit(List<Integer> current_player_card_IDes) {
        int[] player_suitable_indexes = new int[8];
        for (int i = 0; i < 8; i++)
            player_suitable_indexes[i] = allCardsSortedBySuit().indexOf(current_player_card_IDes.get(i));

        List<Integer> sorted_card_IDes = new ArrayList<>();
        Arrays.sort(player_suitable_indexes);

        for (int i = 0; i < 8; i++)
            sorted_card_IDes.add(allCardsSortedBySuit().get(player_suitable_indexes[i]));

        return sorted_card_IDes;
    }

    @NonNull
    private List<Integer> allCardsSortedBySuit() {
        List<Integer> cards_sorted_by_suit = new ArrayList<>();
        for (int i = 0; i < 8; i++)
            cards_sorted_by_suit.add(sortedSpades().get(i));
        for (int i = 0; i < 8; i++)
            cards_sorted_by_suit.add(sortedClubs().get(i));
        for (int i = 0; i < 8; i++)
            cards_sorted_by_suit.add(sortedDiamonds().get(i));
        for (int i = 0; i < 8; i++)
            cards_sorted_by_suit.add(sortedHearts().get(i));

        return cards_sorted_by_suit;
    }

    @NonNull
    private List<Integer> allCardsSortedByValue() {
        List<Integer> cards_sorted_by_value = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            cards_sorted_by_value.add(sortedSpades().get(i));
            cards_sorted_by_value.add(sortedClubs().get(i));
            cards_sorted_by_value.add(sortedDiamonds().get(i));
            cards_sorted_by_value.add(sortedHearts().get(i));
        }

        return cards_sorted_by_value;
    }

    @NonNull
    private List<Integer> generateDeckOfCards() {
        List<Integer> deck = new ArrayList<>();
        // Add each card image resource ID to the deck
        for (int image_id : card_images) {
            deck.add(image_id);
        }
        return deck;
    }

    @NonNull
    private List<Integer> sortedClubs() {
        List<Integer> CLUBS = new ArrayList<>();
        for (int i = 0; i < card_images.length; i += 4) {
            CLUBS.add(card_images[i]);
        }
        return CLUBS;
    }

    @NonNull
    private List<Integer> sortedDiamonds() {
        List<Integer> DIAMONDS = new ArrayList<>();
        for (int i = 1; i < card_images.length; i += 4) {
            DIAMONDS.add(card_images[i]);
        }
        return DIAMONDS;
    }

    @NonNull
    private List<Integer> sortedHearts() {
        List<Integer> HEARTS = new ArrayList<>();
        for (int i = 2; i < card_images.length; i += 4) {
            HEARTS.add(card_images[i]);
        }
        return HEARTS;
    }

    @NonNull
    private List<Integer> sortedSpades() {
        List<Integer> SPADES = new ArrayList<>();
        for (int i = 3; i < card_images.length; i += 4) {
            SPADES.add(card_images[i]);
        }
        return SPADES;
    }

}