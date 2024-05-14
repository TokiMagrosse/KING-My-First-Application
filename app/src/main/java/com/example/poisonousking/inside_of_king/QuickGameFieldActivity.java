package com.example.poisonousking.inside_of_king;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

import com.example.poisonousking.R;
import com.example.poisonousking.helper_classes.Deck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuickGameFieldActivity extends AppCompatActivity {

    protected Button[] turners = new Button[3];
    TextView[] scoreViews = new TextView[4];
    private final CardView[] userCardDoorViews = new CardView[8];
    ImageView[] fourCenterCellViews = new ImageView[4];
    private final ImageView[] userCardViews = new ImageView[8];
    Button table_button, menu_button;
    List<Integer> Spades = Deck.sortedSpades();
    List<Integer> Clubs = Deck.sortedClubs();
    List<Integer> Diamonds = Deck.sortedDiamonds();
    List<Integer> Hearts = Deck.sortedHearts();
    List<Integer> userCards, userSpades, userClubs, userDiamonds, userHearts;
    List<Integer> firstBotCards, firstBotSpades, firstBotClubs, firstBotDiamonds, firstBotHearts;
    List<Integer> secondBotCards, secondBotSpades, secondBotClubs, secondBotDiamonds, secondBotHearts;
    List<Integer> thirdBotCards, thirdBotSpades, thirdBotClubs, thirdBotDiamonds, thirdBotHearts;
    private static final Random randomizer = new Random();
    protected static List<Integer> fourCycle = new ArrayList<>();
    public int[] playersScores = {0, 0, 0, 0};
    public static int winnerOfCorrespondingTrick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quick_game_field);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupQuickGameKing();
    }

    private void initializeViews() {
        table_button = findViewById(R.id.table_button);
        menu_button = findViewById(R.id.menu_button);

        turners[0] = findViewById(R.id.first_bot_turner);
        turners[1] = findViewById(R.id.second_bot_turner);
        turners[2] = findViewById(R.id.third_bot_turner);

        // Initialize score views
        scoreViews[0] = findViewById(R.id.score_view_1);
        scoreViews[1] = findViewById(R.id.score_view_2);
        scoreViews[2] = findViewById(R.id.score_view_3);
        scoreViews[3] = findViewById(R.id.score_view_4);

        // Initialize user card views
        userCardViews[0] = findViewById(R.id.my_card_1);
        userCardViews[1] = findViewById(R.id.my_card_2);
        userCardViews[2] = findViewById(R.id.my_card_3);
        userCardViews[3] = findViewById(R.id.my_card_4);
        userCardViews[4] = findViewById(R.id.my_card_5);
        userCardViews[5] = findViewById(R.id.my_card_6);
        userCardViews[6] = findViewById(R.id.my_card_7);
        userCardViews[7] = findViewById(R.id.my_card_8);

        // Initialize center card views
        fourCenterCellViews[0] = findViewById(R.id.center_card_1);
        fourCenterCellViews[1] = findViewById(R.id.center_card_2);
        fourCenterCellViews[2] = findViewById(R.id.center_card_3);
        fourCenterCellViews[3] = findViewById(R.id.center_card_4);

        // Initialize user card "door" views
        userCardDoorViews[0] = findViewById(R.id.card_door_1);
        userCardDoorViews[1] = findViewById(R.id.card_door_2);
        userCardDoorViews[2] = findViewById(R.id.card_door_3);
        userCardDoorViews[3] = findViewById(R.id.card_door_4);
        userCardDoorViews[4] = findViewById(R.id.card_door_5);
        userCardDoorViews[5] = findViewById(R.id.card_door_6);
        userCardDoorViews[6] = findViewById(R.id.card_door_7);
        userCardDoorViews[7] = findViewById(R.id.card_door_8);
    }

    private void setupQuickGameKing() {
        // Prepare the deck and distribute cards
        List<Integer> deck = new ArrayList<>();
        for (int card : Deck.deckOfCards) {
            deck.add(card);  // Add each card individually
        }
        Collections.shuffle(deck);  // Shuffle the deck

        // User cards sorted by suit and division into 4 parts:
        userCards = Deck.currentPlayerCardsSortedBySuit(deck.subList(0, 8));
        userSpades = Deck.currentPlayerSpades(userCards); // User spades, clubs, diamonds, hearts
        userClubs = Deck.currentPlayerClubs(userCards);
        userDiamonds = Deck.currentPlayerDiamonds(userCards);
        userHearts = Deck.currentPlayerHearts(userCards);

        // First Bot cards sorted by suit and division into 4 parts:
        firstBotCards = Deck.currentPlayerCardsSortedBySuit(deck.subList(8, 16));
        firstBotSpades = Deck.currentPlayerSpades(firstBotCards); // First Bot spades, clubs, diamonds, hearts
        firstBotClubs = Deck.currentPlayerClubs(firstBotCards);
        firstBotDiamonds = Deck.currentPlayerDiamonds(firstBotCards);
        firstBotHearts = Deck.currentPlayerHearts(firstBotCards);

        // Second Bot cards sorted by suit and division into 4 parts:
        secondBotCards = Deck.currentPlayerCardsSortedBySuit(deck.subList(16, 24));
        secondBotSpades = Deck.currentPlayerSpades(secondBotCards); // Second Bot spades, clubs, diamonds, hearts
        secondBotClubs = Deck.currentPlayerClubs(secondBotCards);
        secondBotDiamonds = Deck.currentPlayerDiamonds(secondBotCards);
        secondBotHearts = Deck.currentPlayerHearts(secondBotCards);

        // Third Bot cards sorted by suit and division into 4 parts:
        thirdBotCards = Deck.currentPlayerCardsSortedBySuit(deck.subList(24, 32));
        thirdBotSpades = Deck.currentPlayerSpades(thirdBotCards); // Third Bot spades, clubs, diamonds, hearts
        thirdBotClubs = Deck.currentPlayerClubs(thirdBotCards);
        thirdBotDiamonds = Deck.currentPlayerDiamonds(thirdBotCards);
        thirdBotHearts = Deck.currentPlayerHearts(thirdBotCards);

        // Show up user's cards
        new Handler().postDelayed(() -> {
            for (int i = 0; i < 8; i++) {
                int finalI = i;
                new Handler().postDelayed(() -> {
                    userCardViews[finalI].setImageResource(userCards.get(finalI));
                    userCardViews[finalI].setVisibility(View.VISIBLE);
                }, 200);
            }
        }, 2500);

        // Set up user interactions
        enableUserCardClicks();
    }

    private void enableUserCardClicks() {
        for (int i = 0; i < userCards.size(); i++) {
            int cardIndex = i;
            userCardViews[i].setOnClickListener(v -> userTurn(cardIndex));
        }

        turners[2].setOnClickListener(v -> {
            userCards.clear();
            setupQuickGameKing();
        });
    }

    private void userTurn(int cardIndex) {
        fourCenterCellViews[0].setImageResource(userCards.get(cardIndex));
        fourCenterCellViews[0].setVisibility(View.VISIBLE);
        userCardViews[cardIndex].setVisibility(View.GONE);
        userCardDoorViews[cardIndex].setVisibility(View.GONE);
        fourCycle.add(userCards.get(cardIndex));
        disableUserCardClicks();
        new Handler().postDelayed(this::botsTurn, 1000);
    }

    private void disableUserCardClicks() {
        for (ImageView cardView : userCardViews) {
            cardView.setClickable(false);
        }
    }

    private void botsTurn() {
        new Handler().postDelayed(() -> {
            turners[0].setVisibility(View.VISIBLE);
            botTurn(fourCycle.get(0), firstBotCards, firstBotSpades, firstBotClubs, firstBotDiamonds, firstBotHearts, 1);
        }, 1250); // First bot turn

        new Handler().postDelayed(() -> {
            turners[0].setVisibility(View.INVISIBLE);
            turners[1].setVisibility(View.VISIBLE);
            botTurn(fourCycle.get(0), secondBotCards, secondBotSpades, secondBotClubs, secondBotDiamonds, secondBotHearts, 2);
        }, 2500); // Second bot turn

        new Handler().postDelayed(() -> {
            turners[1].setVisibility(View.INVISIBLE);
            turners[2].setVisibility(View.VISIBLE);
            botTurn(fourCycle.get(0), thirdBotCards, thirdBotSpades, thirdBotClubs, thirdBotDiamonds, thirdBotHearts, 3);

            winnerOfCorrespondingTrick = determineTheWinnerOfTrick(fourCycle);
            if (fourCycle.contains(R.drawable.king_of_hearts))
                playersScores[winnerOfCorrespondingTrick] -= 70; // Actually it's a lost but...
            else
                playersScores[winnerOfCorrespondingTrick] += 10; // Winner of that trick gets +10 points
        }, 3750); // Third bot turn

        new Handler().postDelayed(() -> scoreViews[winnerOfCorrespondingTrick].setText(String.valueOf(playersScores[winnerOfCorrespondingTrick])), 4500);

        new Handler().postDelayed(this::clearCenterCardsFromCenterView, 5500);
        // new Handler().postDelayed(() -> turners[2].setVisibility(View.INVISIBLE), 5500);

        // Moving center cards to trash bin
        new Handler().postDelayed(() -> {
            userCards.set(userCards.indexOf(fourCycle.get(0)), -1);
            Deck.cardSuitList(fourCycle.get(0), userSpades, userClubs, userDiamonds, userHearts).
                    set(Deck.cardSuitList(fourCycle.get(0), userSpades, userClubs, userDiamonds, userHearts).indexOf(fourCycle.get(0)),
                            null);

            firstBotCards.remove(fourCycle.get(1));
            Deck.cardSuitList(fourCycle.get(1), firstBotSpades, firstBotClubs, firstBotDiamonds, firstBotHearts).remove(fourCycle.get(1));

            secondBotCards.remove(fourCycle.get(2));
            Deck.cardSuitList(fourCycle.get(2), secondBotSpades, secondBotClubs, secondBotDiamonds, secondBotHearts).remove(fourCycle.get(2));

            thirdBotCards.remove(fourCycle.get(3));
            Deck.cardSuitList(fourCycle.get(3), thirdBotSpades, thirdBotClubs, thirdBotDiamonds, thirdBotHearts).remove(fourCycle.get(3));

            fourCycle.clear();
        }, 5500);

        new Handler().postDelayed(this::enableUserCardClicks, 6000);  // Re-enable user clicks after bots have played
    }

    private int determineTheWinnerOfTrick(@NonNull List<Integer> fourCenterCardIDes) {
        int[] indexesInFirstCenterCardSuit = new int[4];
        Arrays.fill(indexesInFirstCenterCardSuit, -1);  // Initialize with -1

        List<Integer> firstCardSuitList = Deck.cardSuitList(fourCenterCardIDes.get(0), Spades, Clubs, Diamonds, Hearts);
        Log.d("Debug", "First card suit list: " + firstCardSuitList.toString());

        for (byte j = 0; j < fourCenterCardIDes.size(); j++) {
            if (firstCardSuitList.contains(fourCenterCardIDes.get(j))) {
                indexesInFirstCenterCardSuit[j] = firstCardSuitList.indexOf(fourCenterCardIDes.get(j));
            }
            Log.d("Debug", "Card " + j + ": " + fourCenterCardIDes.get(j) + " Index in suit: " + indexesInFirstCenterCardSuit[j]);
        }

        int max = -1;
        int maxIndex = -1;
        for (int j = 0; j < fourCenterCardIDes.size(); j++) {
            if (indexesInFirstCenterCardSuit[j] != -1 && indexesInFirstCenterCardSuit[j] > max) {
                max = indexesInFirstCenterCardSuit[j];
                maxIndex = j;
            }
        }
        Log.d("Debug", "Winner index: " + maxIndex);

        return maxIndex;
    }

    private void clearCenterCardsFromCenterView() {
        for (ImageView centerCellView : fourCenterCellViews)
            centerCellView.setVisibility(View.INVISIBLE);
    }

    private void botTurn(int userCardID, @NonNull List<Integer> botCards, List<Integer> botSpades, List<Integer> botClubs,
                         List<Integer> botDiamonds, List<Integer> botHearts, int botCellIndex) {
        int botCurrentCardID, list_size = botCards.size();

        // Checking the user card suit so bot can determine what card to throw
        if (Spades.contains(userCardID)) {
            if (botSpades.isEmpty()) {
                if (botCards.contains(R.drawable.king_of_hearts)) {
                    moveCardToCenter(R.drawable.king_of_hearts, botCellIndex, fourCycle);
                } else {
                    botCurrentCardID = botCards.get(randomizer.nextInt(list_size));
                    moveCardToCenter(botCurrentCardID, botCellIndex, fourCycle);
                }
            } else {
                int rand_spades = botSpades.size();
                botCurrentCardID = botSpades.get(randomizer.nextInt(rand_spades));
                moveCardToCenter(botCurrentCardID, botCellIndex, fourCycle);
            }
        }

        if (Clubs.contains(userCardID)) {
            if (botClubs.isEmpty()) {
                if (botCards.contains(R.drawable.king_of_hearts)) {
                    moveCardToCenter(R.drawable.king_of_hearts, botCellIndex, fourCycle);
                } else {
                    botCurrentCardID = botCards.get(randomizer.nextInt(list_size));
                    moveCardToCenter(botCurrentCardID, botCellIndex, fourCycle);
                }
            } else {
                int rand_clubs = botClubs.size();
                botCurrentCardID = botClubs.get(randomizer.nextInt(rand_clubs));
                moveCardToCenter(botCurrentCardID, botCellIndex, fourCycle);
            }
        }

        if (Diamonds.contains(userCardID)) {
            if (botDiamonds.isEmpty()) {
                if (botCards.contains(R.drawable.king_of_hearts)) {
                    moveCardToCenter(R.drawable.king_of_hearts, botCellIndex, fourCycle);
                } else {
                    botCurrentCardID = botCards.get(randomizer.nextInt(list_size));
                    moveCardToCenter(botCurrentCardID, botCellIndex, fourCycle);
                }
            } else {
                int rand_diamonds = botDiamonds.size();
                botCurrentCardID = botDiamonds.get(randomizer.nextInt(rand_diamonds));
                moveCardToCenter(botCurrentCardID, botCellIndex, fourCycle);
            }
        }

        if (Hearts.contains(userCardID)) {
            if (botHearts.isEmpty()) {
                botCurrentCardID = botCards.get(randomizer.nextInt(list_size));
            } else {
                int rand_hearts = botHearts.size();
                botCurrentCardID = botHearts.get(randomizer.nextInt(rand_hearts));
            }
            moveCardToCenter(botCurrentCardID, botCellIndex, fourCycle);
        }
    }

    private void moveCardToCenter(Integer card, int playerIndex, @NonNull List<Integer> four_cycle) {
        // Visual logic to move card to center, perhaps setting image resources
        ImageView centerView = fourCenterCellViews[playerIndex];
        centerView.setImageResource(card);
        centerView.setVisibility(View.VISIBLE);
        four_cycle.add(card);
    }
}
