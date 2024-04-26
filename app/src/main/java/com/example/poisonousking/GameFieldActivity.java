package com.example.poisonousking;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GameFieldActivity extends AppCompatActivity {

    protected CardView[] turners = new CardView[3];
    public int[] initial_scores = {0, 0, 0, 0};
    TextView[] scores = new TextView[4];
    private final CardView[] user_card_door_views = new CardView[8];
    ImageView[] four_center_cell_views = new ImageView[4];
    private final ImageView[] user_card_image_views = new ImageView[8];
    Button table_button, menu_button;
    private static final int[] card_images = {
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

        turners[0] = findViewById(R.id.first_bot_turner);
        turners[1] = findViewById(R.id.second_bot_turner);
        turners[2] = findViewById(R.id.third_bot_turner);

        scores[0] = findViewById(R.id.user_score);
        scores[1] = findViewById(R.id.first_bot_score);
        scores[2] = findViewById(R.id.second_bot_score);
        scores[3] = findViewById(R.id.third_bot_score);
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

        /* Dividing user cards to 4 parts: sorted spades, clubs, diamonds, hearts */
        List<Integer> user_sorted_by_suit = currentPlayerCardsSortedBySuit(user_cards_IDes);

        // Four lists of IDes separately for each suitable sorted part for User
        List<Integer> user_spades = new ArrayList<>();
        List<Integer> user_clubs = new ArrayList<>();
        List<Integer> user_diamonds = new ArrayList<>();
        List<Integer> user_hearts = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            if (SPADES.contains(user_sorted_by_suit.get(i)))
                user_spades.add(user_sorted_by_suit.get(i));
            if (CLUBS.contains(user_sorted_by_suit.get(i)))
                user_clubs.add(user_sorted_by_suit.get(i));
            if (DIAMONDS.contains(user_sorted_by_suit.get(i)))
                user_diamonds.add(user_sorted_by_suit.get(i));
            if (HEARTS.contains(user_sorted_by_suit.get(i)))
                user_hearts.add(user_sorted_by_suit.get(i));
        }

        /*--------------------------Distribution of user cards by random------------------------------*/
        new Handler().postDelayed(() -> {
            for (int i = 0; i < 8; i++) {
                int finalI = i;
                new Handler().postDelayed(() -> {
                    user_card_image_views[finalI].setImageResource(user_sorted_by_suit.get(finalI));
                }, 200);
            }
        }, 1500);

        /* Dividing first bot cards to 4 parts: sorted spades, clubs, diamonds, hearts */
        List<Integer> P1_sorted_by_suit = currentPlayerCardsSortedBySuit(first_bot_cards_IDes);

        // Four lists of IDes separately for each suitable sorted part for BOT-P1
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

        /* Dividing second bot cards to 4 parts: sorted spades, clubs, diamonds, hearts */
        List<Integer> P2_sorted_by_suit = currentPlayerCardsSortedBySuit(second_bot_cards_IDes);

        // Four lists of IDes separately for each suitable sorted part for BOT-P2
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

        /* Dividing third bot cards to 4 parts: sorted spades, clubs, diamonds, hearts */
        List<Integer> P3_sorted_by_suit = currentPlayerCardsSortedBySuit(third_bot_cards_IDEs);

        // Four lists of IDes separately for each suitable sorted part for BOT-P3
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

        Random random = new Random();
        List<Integer> four_cycle = new ArrayList<>();

        int user_current_card_ID;
        AtomicInteger P1_current_card_ID = new AtomicInteger();
        AtomicInteger P2_current_card_ID = new AtomicInteger();
        AtomicInteger P3_current_card_ID = new AtomicInteger();

        boolean[] cardClickable = new boolean[8];

        for (int round = 0; round < 8; round++) {
            Arrays.fill(cardClickable, true); // Initially, all cards are clickable

            // Game has already started. Good Luck! \\
            for (byte j = 0; j < user_sorted_by_suit.size(); j++) {
                if (cardClickable[j]) {
                    user_current_card_ID = user_sorted_by_suit.get(j);
                    byte finalJ = j;
                    int finalUser_current_card_ID = user_current_card_ID;

                    user_card_image_views[j].setOnClickListener(v -> {
                        four_cycle.add(user_sorted_by_suit.get(finalJ));
                        // Set all cards not clickable
                        Arrays.fill(cardClickable, false);

                        // Set the clicked card clickable
                        cardClickable[finalJ] = true;

                        /* ----------Move user's clicked card to the center---------- */
                        four_center_cell_views[0].setImageDrawable(user_card_image_views[finalJ].getDrawable());
                        four_center_cell_views[0].setVisibility(View.VISIBLE);
                        user_card_image_views[finalJ].setVisibility(View.GONE);
                        user_card_door_views[finalJ].setVisibility(View.GONE);

                        // Set all other cards to be not clickable
                        for (int k = 0; k < 8; k++)
                            if (k != finalJ)
                                user_card_image_views[k].setClickable(false);

                        /* ----------Show the "first_bot" card after the user card has been clicked---------- */
                        if (SPADES.contains(finalUser_current_card_ID)) {
                            if (P1_spades.isEmpty()) {
                                if (P1_sorted_by_suit.contains(R.drawable.king_of_hearts)) {
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[1].setImageResource(R.drawable.king_of_hearts);
                                        four_center_cell_views[1].setVisibility(View.VISIBLE);
                                    }, 2000);
                                    four_cycle.add(R.drawable.king_of_hearts);
                                } else {
                                    int rand_index = P1_sorted_by_suit.size();
                                    P1_current_card_ID.set(P1_sorted_by_suit.get(random.nextInt(rand_index)));
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[1].setImageResource(P1_current_card_ID.get());
                                        four_center_cell_views[1].setVisibility(View.VISIBLE);
                                    }, 2000);
                                    four_cycle.add(P1_current_card_ID.get());
                                }
                            } else {
                                int rand_index = P1_spades.size();
                                P1_current_card_ID.set(P1_spades.get(random.nextInt(rand_index)));
                                new Handler().postDelayed(() -> {
                                    four_center_cell_views[1].setImageResource(P1_current_card_ID.get());
                                    four_center_cell_views[1].setVisibility(View.VISIBLE);
                                }, 2000);
                                four_cycle.add(P1_current_card_ID.get());
                            }
                        }

                        if (CLUBS.contains(finalUser_current_card_ID)) {
                            if (P1_clubs.isEmpty()) {
                                if (P1_sorted_by_suit.contains(R.drawable.king_of_hearts)) {
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[1].setImageResource(R.drawable.king_of_hearts);
                                        four_center_cell_views[1].setVisibility(View.VISIBLE);
                                    }, 2000);
                                    four_cycle.add(R.drawable.king_of_hearts);
                                } else {
                                    int rand_index = P1_sorted_by_suit.size();
                                    P1_current_card_ID.set(P1_sorted_by_suit.get(random.nextInt(rand_index)));
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[1].setImageResource(P1_current_card_ID.get());
                                        four_center_cell_views[1].setVisibility(View.VISIBLE);
                                    }, 2000);
                                    four_cycle.add(P1_current_card_ID.get());
                                }
                            } else {
                                int rand_index = P1_clubs.size();
                                P1_current_card_ID.set(P1_clubs.get(random.nextInt(rand_index)));
                                new Handler().postDelayed(() -> {
                                    four_center_cell_views[1].setImageResource(P1_current_card_ID.get());
                                    four_center_cell_views[1].setVisibility(View.VISIBLE);
                                }, 2000);
                                four_cycle.add(P1_current_card_ID.get());
                            }
                        }

                        if (DIAMONDS.contains(finalUser_current_card_ID)) {
                            if (P1_diamonds.isEmpty()) {
                                if (P1_sorted_by_suit.contains(R.drawable.king_of_hearts)) {
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[1].setImageResource(R.drawable.king_of_hearts);
                                        four_center_cell_views[1].setVisibility(View.VISIBLE);
                                    }, 2000);
                                    four_cycle.add(R.drawable.king_of_hearts);
                                } else {
                                    int rand_index = P1_sorted_by_suit.size();
                                    P1_current_card_ID.set(P1_sorted_by_suit.get(random.nextInt(rand_index)));
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[1].setImageResource(P1_current_card_ID.get());
                                        four_center_cell_views[1].setVisibility(View.VISIBLE);
                                    }, 2000);
                                    four_cycle.add(P1_current_card_ID.get());
                                }
                            } else {
                                int rand_index = P1_diamonds.size();
                                P1_current_card_ID.set(P1_diamonds.get(random.nextInt(rand_index)));
                                new Handler().postDelayed(() -> {
                                    four_center_cell_views[1].setImageResource(P1_current_card_ID.get());
                                    four_center_cell_views[1].setVisibility(View.VISIBLE);
                                }, 2000);
                                four_cycle.add(P1_current_card_ID.get());
                            }
                        }

                        if (HEARTS.contains(finalUser_current_card_ID)) {
                            if (P1_hearts.isEmpty()) {
                                if (P1_sorted_by_suit.contains(R.drawable.king_of_hearts)) {
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[1].setImageResource(R.drawable.king_of_hearts);
                                        four_center_cell_views[1].setVisibility(View.VISIBLE);
                                    }, 2000);
                                    four_cycle.add(R.drawable.king_of_hearts);
                                } else {
                                    int rand_index = P1_sorted_by_suit.size();
                                    P1_current_card_ID.set(P1_sorted_by_suit.get(random.nextInt(rand_index)));
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[1].setImageResource(P1_current_card_ID.get());
                                        four_center_cell_views[1].setVisibility(View.VISIBLE);
                                    }, 2000);
                                    four_cycle.add(P1_current_card_ID.get());
                                }
                            } else {
                                int rand_index = P1_hearts.size();
                                P1_current_card_ID.set(P1_hearts.get(random.nextInt(rand_index)));
                                new Handler().postDelayed(() -> {
                                    four_center_cell_views[1].setImageResource(P1_current_card_ID.get());
                                    four_center_cell_views[1].setVisibility(View.VISIBLE);
                                }, 2000);
                                four_cycle.add(P1_current_card_ID.get());
                            }
                        }

                        /* ----------Show the "second_bot" card after the user card has been clicked---------- */
                        if (SPADES.contains(finalUser_current_card_ID)) {
                            if (P2_spades.isEmpty()) {
                                if (P2_sorted_by_suit.contains(R.drawable.king_of_hearts)) {
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[2].setImageResource(R.drawable.king_of_hearts);
                                        four_center_cell_views[2].setVisibility(View.VISIBLE);
                                    }, 4000);
                                    four_cycle.add(R.drawable.king_of_hearts);
                                } else {
                                    int rand_index = P2_sorted_by_suit.size();
                                    P2_current_card_ID.set(P2_sorted_by_suit.get(random.nextInt(rand_index)));
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[2].setImageResource(P2_current_card_ID.get());
                                        four_center_cell_views[2].setVisibility(View.VISIBLE);
                                    }, 4000);
                                    four_cycle.add(P2_current_card_ID.get());
                                }
                            } else {
                                int rand_index = P2_spades.size();
                                P2_current_card_ID.set(P2_spades.get(random.nextInt(rand_index)));
                                new Handler().postDelayed(() -> {
                                    four_center_cell_views[2].setImageResource(P2_current_card_ID.get());
                                    four_center_cell_views[2].setVisibility(View.VISIBLE);
                                }, 4000);
                                four_cycle.add(P2_current_card_ID.get());
                            }
                        }

                        if (CLUBS.contains(finalUser_current_card_ID)) {
                            if (P2_clubs.isEmpty()) {
                                if (P2_sorted_by_suit.contains(R.drawable.king_of_hearts)) {
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[2].setImageResource(R.drawable.king_of_hearts);
                                        four_center_cell_views[2].setVisibility(View.VISIBLE);
                                    }, 4000);
                                    four_cycle.add(R.drawable.king_of_hearts);
                                } else {
                                    int rand_index = P2_sorted_by_suit.size();
                                    P2_current_card_ID.set(P2_sorted_by_suit.get(random.nextInt(rand_index)));
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[2].setImageResource(P2_current_card_ID.get());
                                        four_center_cell_views[2].setVisibility(View.VISIBLE);
                                    }, 4000);
                                    four_cycle.add(P2_current_card_ID.get());
                                }
                            } else {
                                int rand_index = P2_clubs.size();
                                P2_current_card_ID.set(P2_clubs.get(random.nextInt(rand_index)));
                                new Handler().postDelayed(() -> {
                                    four_center_cell_views[2].setImageResource(P2_current_card_ID.get());
                                    four_center_cell_views[2].setVisibility(View.VISIBLE);
                                }, 4000);
                                four_cycle.add(P2_current_card_ID.get());
                            }
                        }

                        if (DIAMONDS.contains(finalUser_current_card_ID)) {
                            if (P2_diamonds.isEmpty()) {
                                if (P2_sorted_by_suit.contains(R.drawable.king_of_hearts)) {
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[2].setImageResource(R.drawable.king_of_hearts);
                                        four_center_cell_views[2].setVisibility(View.VISIBLE);
                                    }, 4000);
                                    four_cycle.add(R.drawable.king_of_hearts);
                                } else {
                                    int rand_index = P2_sorted_by_suit.size();
                                    P2_current_card_ID.set(P2_sorted_by_suit.get(random.nextInt(rand_index)));
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[2].setImageResource(P2_current_card_ID.get());
                                        four_center_cell_views[2].setVisibility(View.VISIBLE);
                                    }, 4000);
                                    four_cycle.add(P2_current_card_ID.get());
                                }
                            } else {
                                int rand_index = P2_diamonds.size();
                                P2_current_card_ID.set(P2_diamonds.get(random.nextInt(rand_index)));
                                new Handler().postDelayed(() -> {
                                    four_center_cell_views[2].setImageResource(P2_current_card_ID.get());
                                    four_center_cell_views[2].setVisibility(View.VISIBLE);
                                }, 4000);
                                four_cycle.add(P2_current_card_ID.get());
                            }
                        }

                        if (HEARTS.contains(finalUser_current_card_ID)) {
                            if (P2_hearts.isEmpty()) {
                                if (P2_sorted_by_suit.contains(R.drawable.king_of_hearts)) {
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[2].setImageResource(R.drawable.king_of_hearts);
                                        four_center_cell_views[2].setVisibility(View.VISIBLE);
                                    }, 4000);
                                    four_cycle.add(R.drawable.king_of_hearts);
                                } else {
                                    int rand_index = P2_sorted_by_suit.size();
                                    P2_current_card_ID.set(P2_sorted_by_suit.get(random.nextInt(rand_index)));
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[2].setImageResource(P2_current_card_ID.get());
                                        four_center_cell_views[2].setVisibility(View.VISIBLE);
                                    }, 4000);
                                    four_cycle.add(P2_current_card_ID.get());
                                }
                            } else {
                                int rand_index = P2_hearts.size();
                                P2_current_card_ID.set(P2_hearts.get(random.nextInt(rand_index)));
                                new Handler().postDelayed(() -> {
                                    four_center_cell_views[2].setImageResource(P2_current_card_ID.get());
                                    four_center_cell_views[2].setVisibility(View.VISIBLE);
                                }, 4000);
                                four_cycle.add(P2_current_card_ID.get());
                            }
                        }

                        /* ----------Show the "third_bot" card after the user card has been clicked---------- */
                        if (SPADES.contains(finalUser_current_card_ID)) {
                            if (P3_spades.isEmpty()) {
                                if (P3_sorted_by_suit.contains(R.drawable.king_of_hearts)) {
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[3].setImageResource(R.drawable.king_of_hearts);
                                        four_center_cell_views[3].setVisibility(View.VISIBLE);
                                    }, 6000);
                                    four_cycle.add(R.drawable.king_of_hearts);
                                } else {
                                    int rand_index = P3_sorted_by_suit.size();
                                    P3_current_card_ID.set(P3_sorted_by_suit.get(random.nextInt(rand_index)));
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[3].setImageResource(P3_current_card_ID.get());
                                        four_center_cell_views[3].setVisibility(View.VISIBLE);
                                    }, 6000);
                                    four_cycle.add(P3_current_card_ID.get());
                                }
                            } else {
                                int rand_index = P3_spades.size();
                                P3_current_card_ID.set(P3_spades.get(random.nextInt(rand_index)));
                                new Handler().postDelayed(() -> {
                                    four_center_cell_views[3].setImageResource(P3_current_card_ID.get());
                                    four_center_cell_views[3].setVisibility(View.VISIBLE);
                                }, 6000);
                                four_cycle.add(P3_current_card_ID.get());
                            }
                        }

                        if (CLUBS.contains(finalUser_current_card_ID)) {
                            if (P3_clubs.isEmpty()) {
                                if (P3_sorted_by_suit.contains(R.drawable.king_of_hearts)) {
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[3].setImageResource(R.drawable.king_of_hearts);
                                        four_center_cell_views[3].setVisibility(View.VISIBLE);
                                    }, 6000);
                                    four_cycle.add(R.drawable.king_of_hearts);
                                } else {
                                    int rand_index = P3_sorted_by_suit.size();
                                    P3_current_card_ID.set(P3_sorted_by_suit.get(random.nextInt(rand_index)));
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[3].setImageResource(P3_current_card_ID.get());
                                        four_center_cell_views[3].setVisibility(View.VISIBLE);
                                    }, 6000);
                                    four_cycle.add(P3_current_card_ID.get());
                                }
                            } else {
                                int rand_index = P3_clubs.size();
                                P3_current_card_ID.set(P3_clubs.get(random.nextInt(rand_index)));
                                new Handler().postDelayed(() -> {
                                    four_center_cell_views[3].setImageResource(P3_clubs.get(random.nextInt(rand_index)));
                                    four_center_cell_views[3].setVisibility(View.VISIBLE);
                                }, 6000);
                                four_cycle.add(P3_current_card_ID.get());
                            }
                        }

                        if (DIAMONDS.contains(finalUser_current_card_ID)) {
                            if (P3_diamonds.isEmpty()) {
                                if (P3_sorted_by_suit.contains(R.drawable.king_of_hearts)) {
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[3].setImageResource(R.drawable.king_of_hearts);
                                        four_center_cell_views[3].setVisibility(View.VISIBLE);
                                    }, 6000);
                                    four_cycle.add(R.drawable.king_of_hearts);
                                } else {
                                    int rand_index = P3_sorted_by_suit.size();
                                    P3_current_card_ID.set(P3_sorted_by_suit.get(random.nextInt(rand_index)));
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[3].setImageResource(P3_current_card_ID.get());
                                        four_center_cell_views[3].setVisibility(View.VISIBLE);
                                    }, 6000);
                                    four_cycle.add(P3_current_card_ID.get());
                                }
                            } else {
                                int rand_index = P3_diamonds.size();
                                P3_current_card_ID.set(P3_diamonds.get(random.nextInt(rand_index)));
                                new Handler().postDelayed(() -> {
                                    four_center_cell_views[3].setImageResource(P3_diamonds.get(random.nextInt(rand_index)));
                                    four_center_cell_views[3].setVisibility(View.VISIBLE);
                                }, 6000);
                                four_cycle.add(P3_current_card_ID.get());
                            }
                        }

                        if (HEARTS.contains(finalUser_current_card_ID)) {
                            if (P3_hearts.isEmpty()) {
                                if (P3_sorted_by_suit.contains(R.drawable.king_of_hearts)) {
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[3].setImageResource(R.drawable.king_of_hearts);
                                        four_center_cell_views[3].setVisibility(View.VISIBLE);
                                    }, 6000);
                                    four_cycle.add(R.drawable.king_of_hearts);
                                } else {
                                    int rand_index = P3_sorted_by_suit.size();
                                    P3_current_card_ID.set(P3_sorted_by_suit.get(random.nextInt(rand_index)));
                                    new Handler().postDelayed(() -> {
                                        four_center_cell_views[3].setImageResource(P3_current_card_ID.get());
                                        four_center_cell_views[3].setVisibility(View.VISIBLE);
                                    }, 6000);
                                    four_cycle.add(P3_current_card_ID.get());
                                }
                            } else {
                                int rand_index = P3_hearts.size();
                                P3_current_card_ID.set(P3_hearts.get(random.nextInt(rand_index)));
                                new Handler().postDelayed(() -> {
                                    four_center_cell_views[3].setImageResource(P3_current_card_ID.get());
                                    four_center_cell_views[3].setVisibility(View.VISIBLE);
                                }, 6000);
                                four_cycle.add(P3_current_card_ID.get());
                            }
                        }

                        List<Integer> indexes_in_corresponding_suit = new ArrayList<>();

                        // Determine the current suit based on the finalUser_current_card_ID
                        List<Integer> current_suit_list = definePlayersCurrentCardSuit(finalUser_current_card_ID, SPADES, CLUBS, DIAMONDS, HEARTS);

                        // Check each card in four_cycle against the current suit list
                        for (Integer card : four_cycle) {
                            int indexInSuit = current_suit_list != null ? current_suit_list.indexOf(card) : -1;
                            indexes_in_corresponding_suit.add(indexInSuit);
                        }

                        /* ----------Deciding who is the winner of the (first) round---------- */
                        // user -> 0, P1 -> 1, P2 -> 2, P3 -> 3
                        int winner_index_index = getWinnerIndexInIndexes(indexes_in_corresponding_suit);

                        if (four_cycle.contains(R.drawable.king_of_hearts))
                            initial_scores[winner_index_index] -= 40;
                        else
                            initial_scores[winner_index_index] += 10;

                        // Use the winner_index_index safely within the handler
                        new Handler().postDelayed(() -> {
                            scores[winner_index_index].setText(String.valueOf(initial_scores[winner_index_index]));
                        }, 7500);

                        new Handler().postDelayed(() -> {
                            for (int k = 0; k < 4; k++)
                                four_center_cell_views[k].setVisibility(View.INVISIBLE);
                        }, 8750);

                        new Handler().postDelayed(() -> {
                        /* When 4 cards in first round have already thrown, they should be deleted from players "cardIDes" lists
                        ensure that in future there must not be any syntax error so unexpected crashes*/

                            // At first lets do it for user
                            user_sorted_by_suit.remove(four_cycle.get(0));
                            definePlayersCurrentCardSuit(four_cycle.get(0), user_spades, user_clubs,
                                    user_diamonds, user_hearts).remove(four_cycle.get(0));

                            // Then for first bot (P1)
                            P1_sorted_by_suit.remove(four_cycle.get(1));
                            definePlayersCurrentCardSuit(four_cycle.get(1), P1_spades, P1_clubs,
                                    P1_diamonds, P1_hearts).remove(four_cycle.get(1));

                            // Then for second bot (P2)
                            P2_sorted_by_suit.remove(four_cycle.get(2));
                            definePlayersCurrentCardSuit(four_cycle.get(2), P2_spades, P2_clubs,
                                    P2_diamonds, P2_hearts).remove(four_cycle.get(2));

                            // Then for third bot (P3)
                            P3_sorted_by_suit.remove(four_cycle.get(3));
                            definePlayersCurrentCardSuit(four_cycle.get(3), P3_spades, P3_clubs,
                                    P3_diamonds, P3_hearts).remove(four_cycle.get(3));

                            // Clear four_cycle for center 4 cards
                            four_cycle.clear();
                        }, 8900);
                    });
                }
            }

            // Check if the game is over after this round (e.g., if all players are out of cards)
            if (round == 7 || user_sorted_by_suit.isEmpty()) {
                // Perform any end-of-game logic, such as displaying the final scores
                // and announcing the winner.
                break;
            }
        }

    }

    private static List<Integer> definePlayersCurrentCardSuit(Integer cardID, @NonNull List<Integer> CS1, List<Integer> CS2,
                                                              List<Integer> CS3, List<Integer> CS4) {
        List<Integer> current_suit_list = null;
        if (CS1.contains(cardID))
            current_suit_list = CS1;
        else if (CS2.contains(cardID))
            current_suit_list = CS2;
        else if (CS3.contains(cardID))
            current_suit_list = CS3;
        else if (CS4.contains(cardID))
            current_suit_list = CS4;

        return current_suit_list;
    }

    private static int getWinnerIndexInIndexes(@NonNull List<Integer> indexes) {
        int winner_index_index = 0;
        int winner = indexes.get(0);
        for (int k = 0; k < indexes.size(); k++)
            if (indexes.get(k) > winner) {
                winner = indexes.get(k);
                winner_index_index = k;
            }
        return winner_index_index;
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
    private List<Integer> generateDeckOfCards() {
        List<Integer> deck = new ArrayList<>();
        // Add each card image resource ID to the deck
        for (int image_id : card_images) {
            deck.add(image_id);
        }
        return deck;
    }

    @NonNull
    private static List<Integer> sortedClubs() {
        List<Integer> CLUBS = new ArrayList<>();
        for (int i = 0; i < card_images.length; i += 4) {
            CLUBS.add(card_images[i]);
        }
        return CLUBS;
    }

    @NonNull
    private static List<Integer> sortedDiamonds() {
        List<Integer> DIAMONDS = new ArrayList<>();
        for (int i = 1; i < card_images.length; i += 4) {
            DIAMONDS.add(card_images[i]);
        }
        return DIAMONDS;
    }

    @NonNull
    private static List<Integer> sortedHearts() {
        List<Integer> HEARTS = new ArrayList<>();
        for (int i = 2; i < card_images.length; i += 4) {
            HEARTS.add(card_images[i]);
        }
        return HEARTS;
    }

    @NonNull
    private static List<Integer> sortedSpades() {
        List<Integer> SPADES = new ArrayList<>();
        for (int i = 3; i < card_images.length; i += 4) {
            SPADES.add(card_images[i]);
        }
        return SPADES;
    }
}