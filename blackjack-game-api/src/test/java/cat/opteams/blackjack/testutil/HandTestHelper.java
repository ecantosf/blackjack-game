package cat.opteams.blackjack.testutil;

import cat.opteams.blackjack.domain.model.valueobject.Card;
import cat.opteams.blackjack.domain.model.valueobject.Hand;
import cat.opteams.blackjack.domain.model.valueobject.Rank;
import cat.opteams.blackjack.domain.model.valueobject.Suit;

public class HandTestHelper {

    public static Hand createBlackjackHand() {
        Hand hand = new Hand();
        hand = hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand = hand.addCard(new Card(Suit.SPADES, Rank.KING));
        return hand;
    }

    public static Hand createBustHand() {
        Hand hand = new Hand();
        hand = hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        hand = hand.addCard(new Card(Suit.SPADES, Rank.QUEEN));
        hand = hand.addCard(new Card(Suit.DIAMONDS, Rank.FIVE));
        return hand;
    }

    public static Hand createHandWithValue(int targetValue) {
        Hand hand = new Hand();

        if (targetValue == 21) {
            return createBlackjackHand();
        }

        if (targetValue > 21) {
            return createBustHand();
        }

        int currentValue = 0;

        while (currentValue < targetValue) {
            int needed = targetValue - currentValue;

            if (needed >= 10) {
                hand = hand.addCard(new Card(Suit.HEARTS, Rank.TEN));
                currentValue += 10;
            } else if (needed >= 5) {
                hand = hand.addCard(new Card(Suit.HEARTS, getRankForValue(5)));
                currentValue += 5;
            } else {
                hand = hand.addCard(new Card(Suit.HEARTS, getRankForValue(needed)));
                currentValue += needed;
            }
        }

        return hand;
    }

    public static Hand createHandWithAce() {
        Hand hand = new Hand();
        hand = hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand = hand.addCard(new Card(Suit.SPADES, Rank.FIVE));
        return hand;
    }

    public static Hand createEmptyHand() {
        return new Hand();
    }

    private static Rank getRankForValue(int value) {
        return switch (value) {
            case 2 -> Rank.TWO;
            case 3 -> Rank.THREE;
            case 4 -> Rank.FOUR;
            case 5 -> Rank.FIVE;
            case 6 -> Rank.SIX;
            case 7 -> Rank.SEVEN;
            case 8 -> Rank.EIGHT;
            case 9 -> Rank.NINE;
            default -> Rank.TEN;
        };
    }
}
