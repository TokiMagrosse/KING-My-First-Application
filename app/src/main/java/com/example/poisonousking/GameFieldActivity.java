package com.example.poisonousking;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameFieldActivity extends AppCompatActivity {

    ImageView[] myCards = new ImageView[8];
    ImageView[] bot1Cards = new ImageView[8];
    ImageView[] bot2Cards = new ImageView[8];
    ImageView[] bot3Cards = new ImageView[8];
    Dialog dialog_menu, dialog_table;
    protected CardView[] score_boards = new CardView[4];
    protected CardView[] turners = new CardView[3];
    TextView[] scores = new TextView[4];
    private final CardView[] user_card_door_views = new CardView[8];
    ImageView[] fourCenterCellViews = new ImageView[4];
    private final ImageView[] userCardImageViews = new ImageView[8];
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
    List<Integer> userFinalCards, user_spades, user_clubs, user_diamonds, user_hearts;
    List<Integer> BOT1_finalCards, BOT1_spades, BOT1_clubs, BOT1_diamonds, BOT1_hearts;
    List<Integer> BOT2_finalCards, BOT2_spades, BOT2_clubs, BOT2_diamonds, BOT2_hearts;
    List<Integer> BOT3_finalCards, BOT3_spades, BOT3_clubs, BOT3_diamonds, BOT3_hearts;
    private final Random random = new Random();
    protected static List<Integer> fourCycle = new ArrayList<>();
    public int[] initialScores = {0, 0, 0, 0};

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

        initializeViews();

        // Function to shuffle the 32-card deck and distribute to 4 players by random
        List<Integer> deck = generateDeckOfCards();
        Collections.shuffle(deck);

        // Sublist to divide the deck among players
        List<Integer> user_cards_IDes = deck.subList(0, 8); // Create new lists for each player to avoid sublist modification issues
        List<Integer> first_bot_cards_IDes = deck.subList(8, 16);
        List<Integer> second_bot_cards_IDes = deck.subList(16, 24);
        List<Integer> third_bot_cards_IDEs = deck.subList(24, 32);

        // User cards sorted by suit and division into 4 parts:
        userFinalCards = currentPlayerCardsSortedBySuit(user_cards_IDes);
        user_spades = currentPlayerSpades(userFinalCards); // User spades, clubs, diamonds, hearts
        user_clubs = currentPlayerClubs(userFinalCards);
        user_diamonds = currentPlayerDiamonds(userFinalCards);
        user_hearts = currentPlayerHearts(userFinalCards);

        // First Bot cards sorted by suit and division into 4 parts:
        BOT1_finalCards = currentPlayerCardsSortedBySuit(first_bot_cards_IDes);
        BOT1_spades = currentPlayerSpades(BOT1_finalCards); // First Bot spades, clubs, diamonds, hearts
        BOT1_clubs = currentPlayerClubs(BOT1_finalCards);
        BOT1_diamonds = currentPlayerDiamonds(BOT1_finalCards);
        BOT1_hearts = currentPlayerHearts(BOT1_finalCards);

        // Second Bot cards sorted by suit and division into 4 parts:
        BOT2_finalCards = currentPlayerCardsSortedBySuit(second_bot_cards_IDes);
        BOT2_spades = currentPlayerSpades(BOT2_finalCards); // Second Bot spades, clubs, diamonds, hearts
        BOT2_clubs = currentPlayerClubs(BOT2_finalCards);
        BOT2_diamonds = currentPlayerDiamonds(BOT2_finalCards);
        BOT2_hearts = currentPlayerHearts(BOT2_finalCards);

        // Third Bot cards sorted by suit and division into 4 parts:
        BOT3_finalCards = currentPlayerCardsSortedBySuit(third_bot_cards_IDEs);
        BOT3_spades = currentPlayerSpades(BOT3_finalCards); // Third Bot spades, clubs, diamonds, hearts
        BOT3_clubs = currentPlayerClubs(BOT3_finalCards);
        BOT3_diamonds = currentPlayerDiamonds(BOT3_finalCards);
        BOT3_hearts = currentPlayerHearts(BOT3_finalCards);

        /*--------------------------Distribution of user cards by random------------------------------*/
        new Handler().postDelayed(() -> {
            for (int i = 0; i < 8; i++) {
                int finalI = i;
                new Handler().postDelayed(() -> {
                    new Handler().postDelayed(() -> userCardImageViews[finalI].setImageResource(userFinalCards.get(finalI)), 200);
                    new Handler().postDelayed(() -> myCards[finalI].setImageResource(userFinalCards.get(finalI)), 200);
                    new Handler().postDelayed(() -> bot1Cards[finalI].setImageResource(BOT1_finalCards.get(finalI)), 200);
                    new Handler().postDelayed(() -> bot2Cards[finalI].setImageResource(BOT2_finalCards.get(finalI)), 200);
                    new Handler().postDelayed(() -> bot3Cards[finalI].setImageResource(BOT3_finalCards.get(finalI)), 200);
                }, 200);
            }
        }, 1500);

        int user_current_card_ID;

        boolean[] cardClickable = new boolean[8];
        Arrays.fill(cardClickable, true); // Initially, all cards are clickable

        for (int round = 0; round < 8; round++) {

            // Game has already started. Good Luck!
            for (byte j = 0; j < userFinalCards.size(); j++) {
                if (cardClickable[j]) {
                    byte finalJ = j;
                    user_current_card_ID = userFinalCards.get(finalJ);
                    int finalUserCurrentCardID = user_current_card_ID;

                    userCardImageViews[j].setOnClickListener(v -> {
                        fourCycle.add(userFinalCards.get(finalJ));
                        // Set all cards not clickable
                        Arrays.fill(cardClickable, false);

                        // Set the clicked card clickable
                        cardClickable[finalJ] = true;

                        /* ----------Move user's clicked card to the center---------- */
                        fourCenterCellViews[0].setImageDrawable(userCardImageViews[finalJ].getDrawable());
                        fourCenterCellViews[0].setVisibility(View.VISIBLE);
                        userCardImageViews[finalJ].setVisibility(View.GONE);
                        user_card_door_views[finalJ].setVisibility(View.GONE);

                        // Set all other cards to be not clickable
                        for (int k = 0; k < 8; k++)
                            if (k != finalJ)
                                userCardImageViews[k].setClickable(false);

                        new Handler().postDelayed(() -> {
                            moveCardToCenter(botTurn(finalUserCurrentCardID, BOT1_finalCards, BOT1_spades, BOT1_clubs,
                                    BOT1_diamonds, BOT1_hearts), 1);
                        }, 1500);

                        new Handler().postDelayed(() -> {
                            moveCardToCenter(botTurn(finalUserCurrentCardID, BOT2_finalCards, BOT2_spades, BOT2_clubs,
                                    BOT2_diamonds, BOT2_hearts), 2);
                        }, 3000);

                        new Handler().postDelayed(() -> {
                            moveCardToCenter(botTurn(finalUserCurrentCardID, BOT3_finalCards, BOT3_spades, BOT3_clubs,
                                    BOT3_diamonds, BOT3_hearts), 3);
                        }, 4500);

                        fourCycle.add(botTurn(finalUserCurrentCardID, BOT1_finalCards, BOT1_spades, BOT1_clubs, BOT1_diamonds, BOT1_hearts));
                        fourCycle.add(botTurn(finalUserCurrentCardID, BOT2_finalCards, BOT2_spades, BOT2_clubs, BOT2_diamonds, BOT2_hearts));
                        fourCycle.add(botTurn(finalUserCurrentCardID, BOT3_finalCards, BOT3_spades, BOT3_clubs, BOT3_diamonds, BOT3_hearts));

                        List<Integer> indexesInUserCurrentCardSuit = new ArrayList<>();

                        // Determine the current suit based on the finalUserCurrentCardID
                        List<Integer> currentSuitList = definePlayersCurrentCardSuit(finalUserCurrentCardID, sortedSpades(), sortedClubs(), sortedDiamonds(), sortedHearts());

                        // Check each card in four_cycle against the current suit list
                        for (Integer card : fourCycle) {
                            int indexInSuit = currentSuitList != null ? currentSuitList.indexOf(card) : -1;
                            indexesInUserCurrentCardSuit.add(indexInSuit);
                        }

                        // Deciding who is the winner of the corresponding round
                        int winner = maxNumberIndexInTheArray(indexesInUserCurrentCardSuit);

                        if (fourCycle.contains(R.drawable.king_of_hearts))
                            initialScores[winner] -= 40;
                        else
                            initialScores[winner] += 10;

                        // Use the winner safely within the handler
                        new Handler().postDelayed(() -> {
                            scores[0].setText(String.valueOf(fourCycle.size()));
                            // scores[winner].setText(String.valueOf(initialScores[winner]));
                        }, 5500);

                        // Disappearing of four center cards
                        new Handler().postDelayed(() -> {
                            for (int k = 0; k < 4; k++)
                                fourCenterCellViews[k].setVisibility(View.INVISIBLE);
                        }, 6500);

                        // Four center cards to recycle bin
                        new Handler().postDelayed(() -> {
                            // At first lets do it for user
                            userFinalCards.remove(fourCycle.get(0));
                            definePlayersCurrentCardSuit(fourCycle.get(0), user_spades, user_clubs,
                                    user_diamonds, user_hearts).remove(fourCycle.get(0));

                            // Then for first bot
                            BOT1_finalCards.remove(fourCycle.get(1));
                            definePlayersCurrentCardSuit(fourCycle.get(1), BOT1_spades, BOT1_clubs,
                                    BOT1_diamonds, BOT1_hearts).remove(fourCycle.get(1));

                            // Then for second bot
                            BOT2_finalCards.remove(fourCycle.get(2));
                            definePlayersCurrentCardSuit(fourCycle.get(2), BOT2_spades, BOT2_clubs,
                                    BOT2_diamonds, BOT2_hearts).remove(fourCycle.get(2));

                            // Then for third bot
                            BOT3_finalCards.remove(fourCycle.get(3));
                            definePlayersCurrentCardSuit(fourCycle.get(3), BOT3_spades, BOT3_clubs,
                                    BOT3_diamonds, BOT3_hearts).remove(fourCycle.get(3));
                        }, 7000);
                    });
                }
            }
            // Clear four_cycle for center 4 cards
            fourCycle.clear();
            Arrays.fill(cardClickable, true);

            // Check if the game is over after this round (e.g., if all players are out of cards)
            if (round == 7 || userFinalCards.isEmpty()) {
                // Perform any end-of-game logic, such as displaying the final scores
                // and announcing the winner.
                break;
            }

        }

    }

    private void initializeViews() {
        turners[0] = findViewById(R.id.first_bot_turner);
        turners[1] = findViewById(R.id.second_bot_turner);
        turners[2] = findViewById(R.id.third_bot_turner);

        // All necessary attributes for Big Game Dialog
        dialog_table = new Dialog(GameFieldActivity.this);
        dialog_table.setContentView(R.layout.dialog_score_table);
        Objects.requireNonNull(dialog_table.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_table.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_dialog_bg));
        dialog_table.setCancelable(false);

        Window table_window = dialog_table.getWindow();
        if (table_window != null) {
            table_window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            table_window.setGravity(Gravity.CENTER); // Set the gravity to top
            table_window.setWindowAnimations(R.style.DialogAnimation); // Set the animation
        }

        // Initialize all views and set up the interface
        for (int i = 0; i < 4; i++) {
            scores[i] = findViewById(getResources().getIdentifier("user_score" + i, "id", getPackageName()));
            score_boards[i] = findViewById(getResources().getIdentifier("user_score_board" + i, "id", getPackageName()));
        }
        for (int i = 0; i < 8; i++) {
            userCardImageViews[i] = findViewById(getResources().getIdentifier("my_card_" + (i + 1), "id", getPackageName()));
            user_card_door_views[i] = findViewById(getResources().getIdentifier("card_door_" + (i + 1), "id", getPackageName()));

            myCards[i] = dialog_table.findViewById(getResources().getIdentifier("user_" + (i + 1), "id", getPackageName()));
            bot1Cards[i] = dialog_table.findViewById(getResources().getIdentifier("bot1_" + (i + 1), "id", getPackageName()));
            bot2Cards[i] = dialog_table.findViewById(getResources().getIdentifier("bot2_" + (i + 1), "id", getPackageName()));
            bot3Cards[i] = dialog_table.findViewById(getResources().getIdentifier("bot3_" + (i + 1), "id", getPackageName()));
        }
        for (int i = 0; i < 4; i++) {
            fourCenterCellViews[i] = findViewById(getResources().getIdentifier("center_card_" + (i + 1), "id", getPackageName()));
        }

        table_button = findViewById(R.id.table_button);
        menu_button = findViewById(R.id.menu_button);

        table_button.setOnClickListener(v -> {
            dialog_table.show();
        });

        myCards[7].setOnClickListener(v -> dialog_table.dismiss());
    }

    private int botTurn(int userClickedCardID, @NonNull List<Integer> botCards, List<Integer> botSpades, List<Integer> botClubs,
                         List<Integer> botDiamonds, List<Integer> botHearts) {
        int list_size = botCards.size(), botCurrentCardID = 0;

        // Checking the user card suit so bot can determine what card to throw
        if (sortedSpades().contains(userClickedCardID)) {
            if (botSpades.isEmpty()) {
                if (botCards.contains(R.drawable.king_of_hearts)) {
                    botCurrentCardID = R.drawable.king_of_hearts;
                } else {
                    botCurrentCardID = botCards.get(random.nextInt(list_size));
                }
            } else {
                int rand_spades = botSpades.size();
                botCurrentCardID = botSpades.get(random.nextInt(rand_spades));
            }
            return botCurrentCardID;
        }

        if (sortedClubs().contains(userClickedCardID)) {
            if (botClubs.isEmpty()) {
                if (botCards.contains(R.drawable.king_of_hearts)) {
                    botCurrentCardID = R.drawable.king_of_hearts;
                } else {
                    botCurrentCardID = botCards.get(random.nextInt(list_size));
                }
            } else {
                int rand_clubs = botClubs.size();
                botCurrentCardID = botClubs.get(random.nextInt(rand_clubs));
            }
            return botCurrentCardID;
        }

        if (sortedDiamonds().contains(userClickedCardID)) {
            if (botDiamonds.isEmpty()) {
                if (botCards.contains(R.drawable.king_of_hearts)) {
                    botCurrentCardID = R.drawable.king_of_hearts;
                } else {
                    botCurrentCardID = botCards.get(random.nextInt(list_size));
                }
            } else {
                int rand_diamonds = botDiamonds.size();
                botCurrentCardID = botDiamonds.get(random.nextInt(rand_diamonds));
            }
            return botCurrentCardID;
        }

        if (sortedHearts().contains(userClickedCardID)) {
            if (botHearts.isEmpty()) {
                botCurrentCardID = botCards.get(random.nextInt(list_size));
            } else {
                int rand_hearts = botHearts.size();
                botCurrentCardID = botHearts.get(random.nextInt(rand_hearts));
            }
            return botCurrentCardID;
        }

        return botCurrentCardID;
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

    @Contract(pure = true)
    private int maxNumberIndexInTheArray(@NonNull List<Integer> indexes) {
        int max = indexes.get(0), maxIndex = 0;
        for (byte i = 0; i < indexes.size(); i++)
            if (indexes.get(i) > max) {
                max = indexes.get(i);
                maxIndex = i;
            }

        return maxIndex;
    }


    private void moveCardToCenter(Integer card, int playerIndex) {
        // Visual logic to move card to center, perhaps setting image resources
        ImageView centerView = fourCenterCellViews[playerIndex];
        centerView.setImageResource(card);
        centerView.setVisibility(View.VISIBLE);
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
    private static List<Integer> currentPlayerSpades(@NonNull List<Integer> currentCardIDes) {
        List<Integer> spadesOfCurrentPlayer = new ArrayList<>();
        for (byte i = 0; i < currentCardIDes.size(); i++)
            if (sortedSpades().contains(currentCardIDes.get(i)))
                spadesOfCurrentPlayer.add(currentCardIDes.get(i));

        return spadesOfCurrentPlayer;
    }

    @NonNull
    private static List<Integer> currentPlayerClubs(@NonNull List<Integer> currentCardIDes) {
        List<Integer> clubsOfCurrentPlayer = new ArrayList<>();
        for (byte i = 0; i < currentCardIDes.size(); i++)
            if (sortedClubs().contains(currentCardIDes.get(i)))
                clubsOfCurrentPlayer.add(currentCardIDes.get(i));

        return clubsOfCurrentPlayer;
    }

    @NonNull
    private static List<Integer> currentPlayerDiamonds(@NonNull List<Integer> currentCardIDes) {
        List<Integer> diamondsOfCurrentPlayer = new ArrayList<>();
        for (byte i = 0; i < currentCardIDes.size(); i++)
            if (sortedDiamonds().contains(currentCardIDes.get(i)))
                diamondsOfCurrentPlayer.add(currentCardIDes.get(i));

        return diamondsOfCurrentPlayer;
    }

    @NonNull
    private static List<Integer> currentPlayerHearts(@NonNull List<Integer> currentCardIDes) {
        List<Integer> heartsOfCurrentPlayer = new ArrayList<>();
        for (byte i = 0; i < currentCardIDes.size(); i++)
            if (sortedHearts().contains(currentCardIDes.get(i)))
                heartsOfCurrentPlayer.add(currentCardIDes.get(i));

        return heartsOfCurrentPlayer;
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