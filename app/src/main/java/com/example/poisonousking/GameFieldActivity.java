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

    Dialog dialog_menu, dialog_table;
    protected CardView[] scoreBoards = new CardView[4];
    protected CardView[] turners = new CardView[3];
    TextView[] scores = new TextView[4];
    private final CardView[] userCardDoorViews = new CardView[8];
    ImageView[] fourCenterCellViews = new ImageView[4];
    private final ImageView[] userCardViews = new ImageView[8];
    Button table_button, menu_button;
    private static final int[] initialDeckCards = {
            R.drawable.seven_of_clubs, R.drawable.seven_of_diamonds, R.drawable.seven_of_hearts, R.drawable.seven_of_spades,
            R.drawable.eight_of_clubs, R.drawable.eight_of_diamonds, R.drawable.eight_of_hearts, R.drawable.eight_of_spades,
            R.drawable.nine_of_clubs, R.drawable.nine_of_diamonds, R.drawable.nine_of_hearts, R.drawable.nine_of_spades,
            R.drawable.ten_of_clubs, R.drawable.ten_of_diamonds, R.drawable.ten_of_hearts, R.drawable.ten_of_spades,
            R.drawable.jack_of_clubs, R.drawable.jack_of_diamonds, R.drawable.jack_of_hearts, R.drawable.jack_of_spades,
            R.drawable.queen_of_clubs, R.drawable.queen_of_diamonds, R.drawable.queen_of_hearts, R.drawable.queen_of_spades,
            R.drawable.king_of_clubs, R.drawable.king_of_diamonds, R.drawable.king_of_hearts, R.drawable.king_of_spades,
            R.drawable.ace_of_clubs, R.drawable.ace_of_diamonds, R.drawable.ace_of_hearts, R.drawable.ace_of_spades
    };
    List<Integer> userCards, userSpades, userClubs, userDiamonds, userHearts;
    List<Integer> firstBotCards, firstBotSpades, firstBotClubs, firstBotDiamonds, firstBotHearts;
    List<Integer> secondBotCards, secondBotSpades, secondBotClubs, secondBotDiamonds, secondBotHearts;
    List<Integer> thirdBotCards, thirdBotSpades, thirdBotClubs, thirdBotDiamonds, thirdBotHearts;
    private final Random random = new Random();
    static int userClickedCardIndex;

    protected static List<Integer> fourCycle = new ArrayList<>();
    public int[] playersScores = {0, 0, 0, 0};

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
        setupPoisonousGameKing();
    }

    private void initializeViews() {
        table_button = findViewById(R.id.table_button);
        menu_button = findViewById(R.id.menu_button);

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
            scoreBoards[i] = findViewById(getResources().getIdentifier("user_score_board" + i, "id", getPackageName()));
        }
        for (int i = 0; i < 8; i++) {
            userCardViews[i] = findViewById(getResources().getIdentifier("my_card_" + (i + 1), "id", getPackageName()));
            userCardDoorViews[i] = findViewById(getResources().getIdentifier("card_door_" + (i + 1), "id", getPackageName()));
        }
        for (int i = 0; i < 4; i++) {
            fourCenterCellViews[i] = findViewById(getResources().getIdentifier("center_card_" + (i + 1), "id", getPackageName()));
        }

    }

    private void setupPoisonousGameKing() {
        // Prepare the deck and distribute cards
        List<Integer> deck = new ArrayList<>();
        for (int card : initialDeckCards) {
            deck.add(card);  // Add each card individually
        }
        Collections.shuffle(deck);  // Shuffle the deck

        // User cards sorted by suit and division into 4 parts:
        userCards = currentPlayerCardsSortedBySuit(deck.subList(0, 8));
        userSpades = currentPlayerSpades(userCards); // User spades, clubs, diamonds, hearts
        userClubs = currentPlayerClubs(userCards);
        userDiamonds = currentPlayerDiamonds(userCards);
        userHearts = currentPlayerHearts(userCards);

        // First Bot cards sorted by suit and division into 4 parts:
        firstBotCards = currentPlayerCardsSortedBySuit(deck.subList(8, 16));
        firstBotSpades = currentPlayerSpades(firstBotCards); // First Bot spades, clubs, diamonds, hearts
        firstBotClubs = currentPlayerClubs(firstBotCards);
        firstBotDiamonds = currentPlayerDiamonds(firstBotCards);
        firstBotHearts = currentPlayerHearts(firstBotCards);

        // Second Bot cards sorted by suit and division into 4 parts:
        secondBotCards = currentPlayerCardsSortedBySuit(deck.subList(16, 24));
        secondBotSpades = currentPlayerSpades(secondBotCards); // Second Bot spades, clubs, diamonds, hearts
        secondBotClubs = currentPlayerClubs(secondBotCards);
        secondBotDiamonds = currentPlayerDiamonds(secondBotCards);
        secondBotHearts = currentPlayerHearts(secondBotCards);

        // Third Bot cards sorted by suit and division into 4 parts:
        thirdBotCards = currentPlayerCardsSortedBySuit(deck.subList(24, 32));
        thirdBotSpades = currentPlayerSpades(thirdBotCards); // Third Bot spades, clubs, diamonds, hearts
        thirdBotClubs = currentPlayerClubs(thirdBotCards);
        thirdBotDiamonds = currentPlayerDiamonds(thirdBotCards);
        thirdBotHearts = currentPlayerHearts(thirdBotCards);

        // Show up user's cards
        new Handler().postDelayed(() -> {
            for (int i = 0; i < 8; i++) {
                int finalI = i;
                new Handler().postDelayed(() -> userCardViews[finalI].setImageResource(userCards.get(finalI)), 200);
            }
        }, 1250);

        // Set up user interactions
        enableUserCardClicks();
    }

    private void enableUserCardClicks() {
        for (int i = 0; i < userCardViews.length; i++) {
            final int cardIndex = i;
            userClickedCardIndex = cardIndex;
            fourCycle.add(userCards.get(userClickedCardIndex));
            userCardViews[i].setOnClickListener(v -> userTurn(cardIndex));
        }
    }

    private void userTurn(int cardIndex) {
        fourCenterCellViews[0].setImageDrawable(userCardViews[cardIndex].getDrawable());
        fourCenterCellViews[0].setVisibility(View.VISIBLE);
        userCardViews[cardIndex].setVisibility(View.GONE);
        userCardDoorViews[cardIndex].setVisibility(View.GONE);
        disableUserCardClicks();
        new Handler().postDelayed(this::botsTurn, 1500);
    }

    private void disableUserCardClicks() {
        for (ImageView cardView : userCardViews) {
            cardView.setClickable(false);
        }
    }

    private void botsTurn() {
        botTurn(firstBotCards, firstBotSpades, firstBotClubs, firstBotDiamonds, firstBotHearts, 1);
        new Handler().postDelayed(() -> botTurn(secondBotCards, secondBotSpades, secondBotClubs, secondBotDiamonds, secondBotHearts, 2), 2000);
        new Handler().postDelayed(() -> botTurn(thirdBotCards, thirdBotSpades, thirdBotClubs, thirdBotDiamonds, thirdBotHearts, 3), 3500);

        int[] indexes_in_first_card_suit = new int[4];

        List<Integer> firstCardSuitList = cardSuitList(fourCycle.get(0), sortedSpades(), sortedClubs(), sortedDiamonds(), sortedHearts());
        for (byte j = 0; j < 4; j++) {
            if (firstCardSuitList.contains(fourCycle.get(j)))
                indexes_in_first_card_suit[j] = firstCardSuitList.indexOf(fourCycle.get(j));
            else indexes_in_first_card_suit[j] = -1;
        }

        int winner = maxNumberIndexInTheArray(indexes_in_first_card_suit);
        if (fourCycle.contains(R.drawable.king_of_hearts)) playersScores[winner] -= 40;
        else playersScores[winner] += 10;

        /*new Handler().postDelayed(() -> {
            scores[0].setText(String.valueOf(userClickedCardIndex));
        }, 4000);*/

        /*new Handler().postDelayed(() ->  scores[winner].setText(String.valueOf(initial_scores[winner])),
        4000);*/

        new Handler().postDelayed(() -> {
            for (ImageView fourCenterCellView : fourCenterCellViews)
                fourCenterCellView.setVisibility(View.INVISIBLE);
        }, 4750);

        /*new Handler().postDelayed(() -> {
            user_final_cards.remove(four_cycle.get(0));
            cardSuitList(four_cycle.get(0), user_spades, user_clubs, user_diamonds, user_hearts).remove(four_cycle.get(0));

            BOT1_final_cards.remove(four_cycle.get(1));
            cardSuitList(four_cycle.get(1), BOT1_spades, BOT1_clubs, BOT1_diamonds, BOT1_hearts).remove(four_cycle.get(1));

            BOT2_final_cards.remove(four_cycle.get(2));
            cardSuitList(four_cycle.get(2), BOT2_spades, BOT2_clubs, BOT2_diamonds, BOT2_hearts).remove(four_cycle.get(2));

            BOT3_final_cards.remove(four_cycle.get(3));
            cardSuitList(four_cycle.get(3), BOT3_spades, BOT3_clubs, BOT3_diamonds, BOT3_hearts).remove(four_cycle.get(3));

            four_cycle.clear();
        }, 4500);*/

        new Handler().postDelayed(this::enableUserCardClicks, 5500);  // Re-enable user clicks after bots have played
    }

    private void botTurn(@NonNull List<Integer> botCards, List<Integer> botSpades, List<Integer> botClubs,
                         List<Integer> botDiamonds, List<Integer> botHearts, int botCellIndex) {
        int list_size = botCards.size(), botCurrentCardID;

        // Checking the user card suit so bot can determine what card to throw
        if (sortedSpades().contains(userCards.get(userClickedCardIndex))) {
            if (botSpades.isEmpty()) {
                if (botCards.contains(R.drawable.king_of_hearts)) {
                    moveCardToCenter(R.drawable.king_of_hearts, botCellIndex);
                    fourCycle.add(R.drawable.king_of_hearts);
                } else {
                    botCurrentCardID = botCards.get(random.nextInt(list_size));
                    moveCardToCenter(botCurrentCardID, botCellIndex);
                    fourCycle.add(botCurrentCardID);
                }
            } else {
                int rand_spades = botSpades.size();
                botCurrentCardID = botSpades.get(random.nextInt(rand_spades));
                moveCardToCenter(botCurrentCardID, botCellIndex);
                fourCycle.add(botCurrentCardID);
            }
        }

        if (sortedClubs().contains(userCards.get(userClickedCardIndex))) {
            if (botClubs.isEmpty()) {
                if (botCards.contains(R.drawable.king_of_hearts)) {
                    moveCardToCenter(R.drawable.king_of_hearts, botCellIndex);
                    fourCycle.add(R.drawable.king_of_hearts);
                } else {
                    botCurrentCardID = botCards.get(random.nextInt(list_size));
                    moveCardToCenter(botCurrentCardID, botCellIndex);
                    fourCycle.add(botCurrentCardID);
                }
            } else {
                int rand_clubs = botClubs.size();
                botCurrentCardID = botClubs.get(random.nextInt(rand_clubs));
                moveCardToCenter(botCurrentCardID, botCellIndex);
                fourCycle.add(botCurrentCardID);
            }
        }

        if (sortedDiamonds().contains(userCards.get(userClickedCardIndex))) {
            if (botDiamonds.isEmpty()) {
                if (botCards.contains(R.drawable.king_of_hearts)) {
                    moveCardToCenter(R.drawable.king_of_hearts, botCellIndex);
                    fourCycle.add(R.drawable.king_of_hearts);
                } else {
                    botCurrentCardID = botCards.get(random.nextInt(list_size));
                    moveCardToCenter(botCurrentCardID, botCellIndex);
                    fourCycle.add(botCurrentCardID);
                }
            } else {
                int rand_diamonds = botDiamonds.size();
                botCurrentCardID = botDiamonds.get(random.nextInt(rand_diamonds));
                moveCardToCenter(botCurrentCardID, botCellIndex);
                fourCycle.add(botCurrentCardID);
            }
        }

        if (sortedHearts().contains(userCards.get(userClickedCardIndex))) {
            if (botHearts.isEmpty()) {
                botCurrentCardID = botCards.get(random.nextInt(list_size));
            } else {
                int rand_hearts = botHearts.size();
                botCurrentCardID = botHearts.get(random.nextInt(rand_hearts));
            }
            moveCardToCenter(botCurrentCardID, botCellIndex);
            fourCycle.add(botCurrentCardID);
        }
    }

    private void moveCardToCenter(Integer card, int playerIndex) {
        // Visual logic to move card to center, perhaps setting image resources
        ImageView centerView = fourCenterCellViews[playerIndex];
        centerView.setImageResource(card);
        centerView.setVisibility(View.VISIBLE);
    }

    private List<Integer> cardSuitList(int currentCardID, @NonNull List<Integer> CS1, List<Integer> CS2, List<Integer> CS3, List<Integer> CS4) {
        List<Integer> suitList;
        if (CS1.contains(currentCardID))
            suitList = CS1;
        else if (CS2.contains(currentCardID))
            suitList = CS2;
        else if (CS3.contains(currentCardID))
            suitList = CS3;
        else
            suitList = CS4;

        return suitList;
    }

    @Contract(pure = true)
    private int maxNumberIndexInTheArray(@NonNull int[] indexes) {
        int max = indexes[0], maxIndex = 0;
        for (byte i = 0; i < indexes.length; i++)
            if (indexes[i] > max) {
                max = indexes[i];
                maxIndex = i;
            }

        return maxIndex;
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
    private static List<Integer> sortedClubs() {
        List<Integer> CLUBS = new ArrayList<>();
        for (int i = 0; i < initialDeckCards.length; i += 4) {
            CLUBS.add(initialDeckCards[i]);
        }
        return CLUBS;
    }

    @NonNull
    private static List<Integer> sortedDiamonds() {
        List<Integer> DIAMONDS = new ArrayList<>();
        for (int i = 1; i < initialDeckCards.length; i += 4) {
            DIAMONDS.add(initialDeckCards[i]);
        }
        return DIAMONDS;
    }

    @NonNull
    private static List<Integer> sortedHearts() {
        List<Integer> HEARTS = new ArrayList<>();
        for (int i = 2; i < initialDeckCards.length; i += 4) {
            HEARTS.add(initialDeckCards[i]);
        }
        return HEARTS;
    }

    @NonNull
    private static List<Integer> sortedSpades() {
        List<Integer> SPADES = new ArrayList<>();
        for (int i = 3; i < initialDeckCards.length; i += 4) {
            SPADES.add(initialDeckCards[i]);
        }
        return SPADES;
    }
}
