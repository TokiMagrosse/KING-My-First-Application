package com.example.poisonousking.inside_of_king;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.poisonousking.R;
import com.example.poisonousking.helper_classes.Deck;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class QuickGameFieldActivity extends AppCompatActivity {

    protected String[] titles = {"Newbie", "Beginner", "Trainee", "Student", "Expert",
            "Master", "Veteran", "Captain", "Lord", "KING"};
    String title;
    FirebaseFirestore fStore;
    public int total_games_count, won_games_count, lost_games_count, coins_count, user_rating_number;
    private static final float BUTTON_CLICK_VOLUME = 0.4f; // Set volume level to 35% for background music
    private MediaPlayer cardClickSound, buttonClickSound;
    private static final float USER_CARD_CLICK_VOLUME = 0.3f;
    TextView score_table_title;
    TextView[][] final_score_table = new TextView[4][5];
    ImageView[] badges = new ImageView[4];
    FirebaseAuth auth;
    private static String userID;
    ImageView user_profile_picture;
    Button exit_button, play_again_button;
    Dialog dialog_final_results;
    protected Button[] turners = new Button[3];
    TextView[] scoreViews = new TextView[4];
    private final CardView[] userCardDoorViews = new CardView[8];
    ImageView[] fourCenterCellViews = new ImageView[4];
    private final ImageView[] userCardViews = new ImageView[8];
    Button leave_the_game_forever;
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
    byte currentRound = 0;
    public int[] totalScores = {0, 0, 0, 0};
    public int[][] playersScores = {
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    };
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

        buttonClickSound = MediaPlayer.create(this, R.raw.button_click_sound_1);
        // Set the volume of the mediaPlayer to a lower level (background music volume)
        buttonClickSound.setVolume(BUTTON_CLICK_VOLUME, BUTTON_CLICK_VOLUME);

        cardClickSound = MediaPlayer.create(this, R.raw.user_card_click_sound);
        // Set the volume of the mediaPlayer to a lower level (background music volume)
        cardClickSound.setVolume(USER_CARD_CLICK_VOLUME, USER_CARD_CLICK_VOLUME);

        setupQuickGameKing();
    }

    private void initializeViews() {
        fStore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        leave_the_game_forever = findViewById(R.id.leave_the_game_forever);

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

        getUserStats(userID);

        leave_the_game_forever.setOnClickListener(v -> {
            buttonClickSound.start();
            Intent intent = new Intent(QuickGameFieldActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            dialog_final_results.dismiss();
        });

        // All necessary attributes for Score Table Dialog
        dialog_final_results = new Dialog(QuickGameFieldActivity.this);
        dialog_final_results.setContentView(R.layout.dialog_game_final_results);
        Objects.requireNonNull(dialog_final_results.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_final_results.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_dialog_bg));
        dialog_final_results.setCancelable(false);

        Window results_window = dialog_final_results.getWindow();
        if (results_window != null) {
            results_window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            results_window.setGravity(Gravity.CENTER); // Set the gravity to top
            results_window.setWindowAnimations(R.style.DialogAnimation); // Set the animation
        }

        user_profile_picture = dialog_final_results.findViewById(R.id.user_picture);
        fetchAndDisplayProfileImage(user_profile_picture);

        exit_button = dialog_final_results.findViewById(R.id.exit_the_game);
        play_again_button = dialog_final_results.findViewById(R.id.play_again);

        play_again_button.setOnClickListener(v -> {
            dialog_final_results.dismiss();
            buttonClickSound.start();
            for (int i = 0; i < userCards.size(); i++) {
                userCardViews[i].setVisibility(View.INVISIBLE);
            }
            setupQuickGameKing();
        });

        badges[0] = dialog_final_results.findViewById(R.id.user_badge);
        badges[1] = dialog_final_results.findViewById(R.id.first_bot_badge);
        badges[2] = dialog_final_results.findViewById(R.id.second_bot_badge);
        badges[3] = dialog_final_results.findViewById(R.id.third_bot_badge);

        score_table_title = dialog_final_results.findViewById(R.id.score_table_title);

        final_score_table[0][0] = dialog_final_results.findViewById(R.id.user_round_1);
        final_score_table[1][0] = dialog_final_results.findViewById(R.id.first_bot_round_1);
        final_score_table[2][0] = dialog_final_results.findViewById(R.id.second_bot_round_1);
        final_score_table[3][0] = dialog_final_results.findViewById(R.id.third_bot_round_1);
        final_score_table[0][1] = dialog_final_results.findViewById(R.id.user_round_2);
        final_score_table[1][1] = dialog_final_results.findViewById(R.id.first_bot_round_2);
        final_score_table[2][1] = dialog_final_results.findViewById(R.id.second_bot_round_2);
        final_score_table[3][1] = dialog_final_results.findViewById(R.id.third_bot_round_2);
        final_score_table[0][2] = dialog_final_results.findViewById(R.id.user_round_3);
        final_score_table[1][2] = dialog_final_results.findViewById(R.id.first_bot_round_3);
        final_score_table[2][2] = dialog_final_results.findViewById(R.id.second_bot_round_3);
        final_score_table[3][2] = dialog_final_results.findViewById(R.id.third_bot_round_3);
        final_score_table[0][3] = dialog_final_results.findViewById(R.id.user_round_4);
        final_score_table[1][3] = dialog_final_results.findViewById(R.id.first_bot_round_4);
        final_score_table[2][3] = dialog_final_results.findViewById(R.id.second_bot_round_4);
        final_score_table[3][3] = dialog_final_results.findViewById(R.id.third_bot_round_4);
        final_score_table[0][4] = dialog_final_results.findViewById(R.id.user_total);
        final_score_table[1][4] = dialog_final_results.findViewById(R.id.first_bot_total);
        final_score_table[2][4] = dialog_final_results.findViewById(R.id.second_bot_total);
        final_score_table[3][4] = dialog_final_results.findViewById(R.id.third_bot_total);
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
            userCardViews[i].setOnClickListener(v -> userTurn(cardIndex, 0));
        }
    }

    private void userTurn(int cardIndex, int centerCellIndex) {
        fourCenterCellViews[centerCellIndex].setImageResource(userCards.get(cardIndex));
        cardClickSound.start();
        fourCenterCellViews[centerCellIndex].setVisibility(View.VISIBLE);
        userCardViews[cardIndex].setVisibility(View.GONE);
        userCardDoorViews[cardIndex].setVisibility(View.GONE);
        fourCycle.add(userCards.get(cardIndex));
        disableUserCardClicks();
        new Handler().postDelayed(this::botsTurn, 1000);
    }

    private void userTurnButNotFirst(int cardIndex, int centerCellIndex) {
        fourCenterCellViews[centerCellIndex].setImageResource(userCards.get(cardIndex));
        cardClickSound.start();
        fourCenterCellViews[centerCellIndex].setVisibility(View.VISIBLE);
        userCardViews[cardIndex].setVisibility(View.GONE);
        userCardDoorViews[cardIndex].setVisibility(View.GONE);
        fourCycle.add(userCards.get(cardIndex));
        disableUserCardClicks();
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
        }, 750); // First bot turn

        new Handler().postDelayed(() -> {
            turners[0].setVisibility(View.INVISIBLE);
            turners[1].setVisibility(View.VISIBLE);
            botTurn(fourCycle.get(0), secondBotCards, secondBotSpades, secondBotClubs, secondBotDiamonds, secondBotHearts, 2);
        }, 1500); // Second bot turn

        new Handler().postDelayed(() -> {
            turners[1].setVisibility(View.INVISIBLE);
            turners[2].setVisibility(View.VISIBLE);
            botTurn(fourCycle.get(0), thirdBotCards, thirdBotSpades, thirdBotClubs, thirdBotDiamonds, thirdBotHearts, 3);

            winnerOfCorrespondingTrick = determineTheWinnerOfTrick(fourCycle);
            if (fourCycle.contains(R.drawable.king_of_hearts)) {
                totalScores[winnerOfCorrespondingTrick] -= 70;// Actually it's a lost but...
                playersScores[winnerOfCorrespondingTrick][currentRound] -= 70;
            } else {
                totalScores[winnerOfCorrespondingTrick] += 10; // Winner of that trick gets +10 points
                playersScores[winnerOfCorrespondingTrick][currentRound] += 10;
            }
        }, 2250); // Third bot turn

        new Handler().postDelayed(() -> scoreViews[winnerOfCorrespondingTrick].setText(String.valueOf(totalScores[winnerOfCorrespondingTrick])), 3250);

        new Handler().postDelayed(this::clearCenterCardsFromCenterView, 4250);
        new Handler().postDelayed(() -> turners[2].setVisibility(View.INVISIBLE), 4250);

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
        }, 4250);

        new Handler().postDelayed(() -> {
            if (thirdBotCards.isEmpty()) {
                for (int j = 0; j < 4; j++) {
                    final_score_table[j][4].setText(String.valueOf(totalScores[j]));
                    final_score_table[j][currentRound].setText(String.valueOf(playersScores[j][currentRound]));
                }
                if (currentRound == 0)
                    score_table_title.setText(R.string.first_round);
                else if (currentRound == 1)
                    score_table_title.setText(R.string.second_round);
                else if (currentRound == 2)
                    score_table_title.setText(R.string.third_round);
                else {
                    score_table_title.setText(R.string.final_round);
                    play_again_button.setVisibility(View.GONE);
                    exit_button.setVisibility(View.VISIBLE);

                    ratingProgressOrRegress(userID, totalScores);
                    if (gameWinnerOrMaxNumberIndexes(totalScores).contains(0))
                        updateGameCountsOnWin(userID);
                    else
                        updateGameCountsOnLoss(userID);

                    titleUpOrDown(userID, title);

                    exit_button.setOnClickListener(v -> {
                        buttonClickSound.start();
                        Intent intent = new Intent(QuickGameFieldActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        dialog_final_results.dismiss();
                    });
                    for (int w = 0; w < gameWinnerOrMaxNumberIndexes(totalScores).size(); w++)
                        badges[gameWinnerOrMaxNumberIndexes(totalScores).get(w)].setVisibility(View.VISIBLE);
                }
                dialog_final_results.show();
                currentRound++;
            }
        }, 5500);

        new Handler().postDelayed(this::enableUserCardClicks, 4500);  // Re-enable user clicks after bots have played
    }

    @NonNull
    @Contract(pure = true)
    private List<Integer> gameWinnerOrMaxNumberIndexes(@NonNull int[] finalScores) {
        List<Integer> indexesOfMaxNumbers = new ArrayList<>();
        int max = finalScores[0];
        for (int finalScore : finalScores)
            if (max < finalScore)
                max = finalScore;
        for (int j = 0; j < finalScores.length; j++)
            if (finalScores[j] == max)
                indexesOfMaxNumbers.add(j);

        return indexesOfMaxNumbers;
    }

    @Contract(pure = true)
    private int ratingUpOrDown(@NonNull int[] totalScores) {
        return (int) (1.23052007 * ((totalScores[0] + (totalScores[1] + totalScores[2] + totalScores[3]) / 3) / 2));
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

    private void botTurn(int firstCenterCardID, @NonNull List<Integer> playerCards, List<Integer> playerSpades, List<Integer> playerClubs,
                         List<Integer> playerDiamonds, List<Integer> playerHearts, int centerCellIndex) {
        int botCurrentCardID, list_size = playerCards.size();

        // Checking the first player card suit so others can determine what card to throw
        if (Spades.contains(firstCenterCardID)) {
            if (playerSpades.isEmpty()) {
                if (playerCards.contains(R.drawable.king_of_hearts)) {
                    moveCardToCenter(R.drawable.king_of_hearts, centerCellIndex, fourCycle);
                } else {
                    botCurrentCardID = playerCards.get(randomizer.nextInt(list_size));
                    moveCardToCenter(botCurrentCardID, centerCellIndex, fourCycle);
                }
            } else {
                int rand_spades = playerSpades.size();
                botCurrentCardID = playerSpades.get(randomizer.nextInt(rand_spades));
                moveCardToCenter(botCurrentCardID, centerCellIndex, fourCycle);
            }
        }

        if (Clubs.contains(firstCenterCardID)) {
            if (playerClubs.isEmpty()) {
                if (playerCards.contains(R.drawable.king_of_hearts)) {
                    moveCardToCenter(R.drawable.king_of_hearts, centerCellIndex, fourCycle);
                } else {
                    botCurrentCardID = playerCards.get(randomizer.nextInt(list_size));
                    moveCardToCenter(botCurrentCardID, centerCellIndex, fourCycle);
                }
            } else {
                int rand_clubs = playerClubs.size();
                botCurrentCardID = playerClubs.get(randomizer.nextInt(rand_clubs));
                moveCardToCenter(botCurrentCardID, centerCellIndex, fourCycle);
            }
        }

        if (Diamonds.contains(firstCenterCardID)) {
            if (playerDiamonds.isEmpty()) {
                if (playerCards.contains(R.drawable.king_of_hearts)) {
                    moveCardToCenter(R.drawable.king_of_hearts, centerCellIndex, fourCycle);
                } else {
                    botCurrentCardID = playerCards.get(randomizer.nextInt(list_size));
                    moveCardToCenter(botCurrentCardID, centerCellIndex, fourCycle);
                }
            } else {
                int rand_diamonds = playerDiamonds.size();
                botCurrentCardID = playerDiamonds.get(randomizer.nextInt(rand_diamonds));
                moveCardToCenter(botCurrentCardID, centerCellIndex, fourCycle);
            }
        }

        if (Hearts.contains(firstCenterCardID)) {
            if (playerHearts.isEmpty()) {
                botCurrentCardID = playerCards.get(randomizer.nextInt(list_size));
            } else {
                int rand_hearts = playerHearts.size();
                botCurrentCardID = playerHearts.get(randomizer.nextInt(rand_hearts));
            }
            moveCardToCenter(botCurrentCardID, centerCellIndex, fourCycle);
        }
    }

    private void ifTheTrickWinsFirstBot() {
        disableUserCardClicks();
        int firstBotRandomCardID;
        int randCard = firstBotCards.size();
        firstBotRandomCardID = firstBotCards.get(randomizer.nextInt(randCard));

        new Handler().postDelayed(() -> moveCardToCenter(firstBotRandomCardID, 0, fourCycle), 750);

        new Handler().postDelayed(() -> {
            turners[0].setVisibility(View.INVISIBLE);
            turners[1].setVisibility(View.VISIBLE);
            botTurn(firstBotRandomCardID, secondBotCards, secondBotSpades, secondBotClubs, secondBotDiamonds, secondBotHearts, 1);
        }, 1500);

        new Handler().postDelayed(() -> {
            turners[1].setVisibility(View.INVISIBLE);
            turners[2].setVisibility(View.VISIBLE);
            botTurn(firstBotRandomCardID, thirdBotCards, thirdBotSpades, thirdBotClubs, thirdBotDiamonds, thirdBotHearts, 2);
        }, 2750);

        new Handler().postDelayed(() -> {
            turners[2].setVisibility(View.INVISIBLE);
            for (int i = 0; i < userCards.size(); i++) {
                int cardIndex = i;
                userCardViews[i].setOnClickListener(v -> userTurnButNotFirst(cardIndex, 3));
            }
            if (fourCycle.size() == 4) {
                winnerOfCorrespondingTrick = determineTheWinnerOfTrick(fourCycle);
                if (fourCycle.contains(R.drawable.king_of_hearts)) {
                    totalScores[winnerOfCorrespondingTrick] -= 70;// Actually it's a lost but...
                    playersScores[winnerOfCorrespondingTrick][currentRound] -= 70;
                } else {
                    totalScores[winnerOfCorrespondingTrick] += 10; // Winner of that trick gets +10 points
                    playersScores[winnerOfCorrespondingTrick][currentRound] += 10;
                }
            }
        }, 3000);

        new Handler().postDelayed(() -> scoreViews[winnerOfCorrespondingTrick].setText(String.valueOf(totalScores[winnerOfCorrespondingTrick])), 3500);

        new Handler().postDelayed(this::clearCenterCardsFromCenterView, 4000);

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
        }, 4250);

    }

    private void ifTheTrickWinsSecondBot() {
        // NO LOGIC YET...
    }

    private void ifTheTrickWinsThirdBot() {
        // NO LOGIC YET...
    }

    private void moveCardToCenter(Integer card, int playerIndex, @NonNull List<Integer> four_cycle) {
        // Visual logic to move card to center, perhaps setting image resources
        ImageView centerView = fourCenterCellViews[playerIndex];
        centerView.setImageResource(card);
        cardClickSound.start();
        centerView.setVisibility(View.VISIBLE);
        four_cycle.add(card);
    }

    private void getUserStats(String documentId) {
        DocumentReference docRef = fStore.collection("all my users").document(documentId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    title = Objects.requireNonNull(document.getString("Title")); // *****
                    user_rating_number = Objects.requireNonNull(document.getLong("Rating")).intValue();
                    coins_count = Objects.requireNonNull(document.getLong("Coins")).intValue();
                    total_games_count = Objects.requireNonNull(document.getLong("Total games count")).intValue();
                    won_games_count = Objects.requireNonNull(document.getLong("Won games count")).intValue();
                    lost_games_count = Objects.requireNonNull(document.getLong("Lost games count")).intValue();

                    // Now you can use these variables in your activity
                } else {
                    Log.d("QuickGameActivity", "No such document");
                }
            } else {
                Log.d("QuickGameActivity", "get failed with ", task.getException());
            }
        });
    }

    private void titleUpOrDown(String documentId, String title) {
        if (user_rating_number < 250)
            title = titles[0];
        if (user_rating_number >= 250 && user_rating_number < 350)
            title = titles[1];
        if (user_rating_number >= 350 && user_rating_number < 450)
            title = titles[2];
        if (user_rating_number >= 450 && user_rating_number < 550)
            title = titles[3];
        if (user_rating_number >= 550 && user_rating_number < 700)
            title = titles[4];
        if (user_rating_number >= 700 && user_rating_number < 850)
            title = titles[5];
        if (user_rating_number >= 850 && user_rating_number < 1000)
            title = titles[6];
        if (user_rating_number >= 1000 && user_rating_number < 1200)
            title = titles[7];
        if (user_rating_number >= 1200 && user_rating_number < 1500)
            title = titles[8];
        if (user_rating_number >= 1500) {
            title = titles[9];
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("Title", title);

        fStore.collection("all my users").document(documentId).update(updates)
                .addOnSuccessListener(aVoid -> Log.d("QuickGameActivity", "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w("QuickGameActivity", "Error updating document", e));
    }

    private void ratingProgressOrRegress(String documentId, int[] finalScores) {
        user_rating_number += ratingUpOrDown(finalScores);

        Map<String, Object> updates = new HashMap<>();
        updates.put("Rating", user_rating_number);

        fStore.collection("all my users").document(documentId).update(updates)
                .addOnSuccessListener(aVoid -> Log.d("QuickGameActivity", "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w("QuickGameActivity", "Error updating document", e));
    }

    // Method to update game counts when a user wins
    private void updateGameCountsOnWin(String documentId) {
        coins_count += 300;
        total_games_count++;
        won_games_count++;

        Map<String, Object> updates = new HashMap<>();
        updates.put("Total games count", total_games_count);
        updates.put("Won games count", won_games_count);
        updates.put("Coins", coins_count);

        fStore.collection("all my users").document(documentId).update(updates)
                .addOnSuccessListener(aVoid -> Log.d("QuickGameActivity", "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w("QuickGameActivity", "Error updating document", e));
    }

    // Method to update game counts when a user loses
    private void updateGameCountsOnLoss(String documentId) {
        total_games_count++;
        lost_games_count++;

        Map<String, Object> updates = new HashMap<>();
        updates.put("Total games count", total_games_count);
        updates.put("Lost games count", lost_games_count);

        fStore.collection("all my users").document(documentId).update(updates)
                .addOnSuccessListener(aVoid -> Log.d("QuickGameActivity", "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w("QuickGameActivity", "Error updating document", e));
    }

    private void fetchAndDisplayProfileImage(ImageView imageView) {
        DocumentReference userDocRef = fStore.collection("all my users").document(userID);

        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String imageUrl = documentSnapshot.getString("profileImageUrl");
                if (imageUrl != null) {
                    // Load and display the profile image using Glide
                    Glide.with(this)
                            .load(imageUrl)
                            .into(imageView);
                }
            }
        }).addOnFailureListener(e -> {
            // Handle failure to fetch profile image URL
            Toast.makeText(this, "Failed to fetch profile image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
