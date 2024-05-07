package com.example.poisonousking.helper_classes;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Trick {
    int userCardID, firstBotCardID, secondBotCardID, thirdBotCardID;

    public Trick(int userCard, int firstBotCard, int secondBotCard, int thirdBotCard) {
        this.userCardID = userCard;
        this.firstBotCardID = firstBotCard;
        this.secondBotCardID = secondBotCard;
        this.thirdBotCardID = thirdBotCard;
    }

    public List<Integer> centerCardsCollection() {
        List<Integer> fourCycle = new ArrayList<>();
        fourCycle.add(userCardID);
        fourCycle.add(firstBotCardID);
        fourCycle.add(secondBotCardID);
        fourCycle.add(thirdBotCardID);

        return fourCycle;
    }

    public int getWinner() {
        int[] centerCardIndexesInUserCardSuit = new int[4];
        List<Integer> userCardSuit = Deck.cardSuitList(this.userCardID, Deck.sortedSpades(), Deck.sortedClubs(), Deck.sortedDiamonds(), Deck.sortedHearts());
        for (int i = 0; i < this.centerCardsCollection().size(); i++) {
            if (userCardSuit.contains(this.centerCardsCollection().get(i)))
                centerCardIndexesInUserCardSuit[i] = userCardSuit.indexOf(this.centerCardsCollection().get(i));
            else
                centerCardIndexesInUserCardSuit[i] = -1;
        }

        return getMaxIndexInIndexes(centerCardIndexesInUserCardSuit);
    }

    public static int getMaxIndexInIndexes(@NonNull int[] fourCycleIndexes) {
        int max = fourCycleIndexes[0], maxIndex = 0;

        for (int j = 0; j < 4; j++)
            if (fourCycleIndexes[j] > max) {
                max = fourCycleIndexes[j];
                maxIndex = j;
            }

        return maxIndex;
    }
}
