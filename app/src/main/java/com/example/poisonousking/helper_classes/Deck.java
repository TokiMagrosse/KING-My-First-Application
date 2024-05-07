package com.example.poisonousking.helper_classes;

import androidx.annotation.NonNull;

import com.example.poisonousking.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Deck {
    public static final int[] deckOfCards = {
            R.drawable.seven_of_clubs, R.drawable.seven_of_diamonds, R.drawable.seven_of_hearts, R.drawable.seven_of_spades,
            R.drawable.eight_of_clubs, R.drawable.eight_of_diamonds, R.drawable.eight_of_hearts, R.drawable.eight_of_spades,
            R.drawable.nine_of_clubs, R.drawable.nine_of_diamonds, R.drawable.nine_of_hearts, R.drawable.nine_of_spades,
            R.drawable.ten_of_clubs, R.drawable.ten_of_diamonds, R.drawable.ten_of_hearts, R.drawable.ten_of_spades,
            R.drawable.jack_of_clubs, R.drawable.jack_of_diamonds, R.drawable.jack_of_hearts, R.drawable.jack_of_spades,
            R.drawable.queen_of_clubs, R.drawable.queen_of_diamonds, R.drawable.queen_of_hearts, R.drawable.queen_of_spades,
            R.drawable.king_of_clubs, R.drawable.king_of_diamonds, R.drawable.king_of_hearts, R.drawable.king_of_spades,
            R.drawable.ace_of_clubs, R.drawable.ace_of_diamonds, R.drawable.ace_of_hearts, R.drawable.ace_of_spades
    };

    @NonNull
    public static List<Integer> allCardsSortedBySuit() {
        List<Integer> cardsSortedBySuit = new ArrayList<>();
        for (int i = 0; i < 8; i++)
            cardsSortedBySuit.add(sortedSpades().get(i));
        for (int i = 0; i < 8; i++)
            cardsSortedBySuit.add(sortedClubs().get(i));
        for (int i = 0; i < 8; i++)
            cardsSortedBySuit.add(sortedDiamonds().get(i));
        for (int i = 0; i < 8; i++)
            cardsSortedBySuit.add(sortedHearts().get(i));

        return cardsSortedBySuit;
    }

    public static List<Integer> cardSuitList(int currentCardID, @NonNull List<Integer> CS1, List<Integer> CS2, List<Integer> CS3, List<Integer> CS4) {
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

    @NonNull
    public static List<Integer> currentPlayerCardsSortedBySuit(List<Integer> currentPlayerCardIDes) {
        int[] playerSuitableIndexes = new int[8];
        for (int i = 0; i < 8; i++)
            playerSuitableIndexes[i] = allCardsSortedBySuit().indexOf(currentPlayerCardIDes.get(i));

        List<Integer> sortedCardIDes = new ArrayList<>();
        Arrays.sort(playerSuitableIndexes);

        for (int i = 0; i < 8; i++)
            sortedCardIDes.add(allCardsSortedBySuit().get(playerSuitableIndexes[i]));

        return sortedCardIDes;
    }

    @NonNull
    public static List<Integer> currentPlayerSpades(@NonNull List<Integer> currentCardIDes) {
        List<Integer> spadesOfCurrentPlayer = new ArrayList<>();
        for (byte i = 0; i < currentCardIDes.size(); i++)
            if (sortedSpades().contains(currentCardIDes.get(i)))
                spadesOfCurrentPlayer.add(currentCardIDes.get(i));

        return spadesOfCurrentPlayer;
    }

    @NonNull
    public static List<Integer> currentPlayerClubs(@NonNull List<Integer> currentCardIDes) {
        List<Integer> clubsOfCurrentPlayer = new ArrayList<>();
        for (byte i = 0; i < currentCardIDes.size(); i++)
            if (sortedClubs().contains(currentCardIDes.get(i)))
                clubsOfCurrentPlayer.add(currentCardIDes.get(i));

        return clubsOfCurrentPlayer;
    }

    @NonNull
    public static List<Integer> currentPlayerDiamonds(@NonNull List<Integer> currentCardIDes) {
        List<Integer> diamondsOfCurrentPlayer = new ArrayList<>();
        for (byte i = 0; i < currentCardIDes.size(); i++)
            if (sortedDiamonds().contains(currentCardIDes.get(i)))
                diamondsOfCurrentPlayer.add(currentCardIDes.get(i));

        return diamondsOfCurrentPlayer;
    }

    @NonNull
    public static List<Integer> currentPlayerHearts(@NonNull List<Integer> currentCardIDes) {
        List<Integer> heartsOfCurrentPlayer = new ArrayList<>();
        for (byte i = 0; i < currentCardIDes.size(); i++)
            if (sortedHearts().contains(currentCardIDes.get(i)))
                heartsOfCurrentPlayer.add(currentCardIDes.get(i));

        return heartsOfCurrentPlayer;
    }

    @NonNull
    public static List<Integer> sortedClubs() {
        List<Integer> CLUBS = new ArrayList<>();
        for (int i = 0; i < deckOfCards.length; i += 4) {
            CLUBS.add(deckOfCards[i]);
        }
        return CLUBS;
    }

    @NonNull
    public static List<Integer> sortedDiamonds() {
        List<Integer> DIAMONDS = new ArrayList<>();
        for (int i = 1; i < deckOfCards.length; i += 4) {
            DIAMONDS.add(deckOfCards[i]);
        }
        return DIAMONDS;
    }

    @NonNull
    public static List<Integer> sortedHearts() {
        List<Integer> HEARTS = new ArrayList<>();
        for (int i = 2; i < deckOfCards.length; i += 4) {
            HEARTS.add(deckOfCards[i]);
        }
        return HEARTS;
    }

    @NonNull
    public static List<Integer> sortedSpades() {
        List<Integer> SPADES = new ArrayList<>();
        for (int i = 3; i < deckOfCards.length; i += 4) {
            SPADES.add(deckOfCards[i]);
        }
        return SPADES;
    }
}
