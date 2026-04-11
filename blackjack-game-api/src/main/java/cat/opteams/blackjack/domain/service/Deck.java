package cat.opteams.blackjack.domain.service;

import cat.opteams.blackjack.domain.model.valueobject.Card;
import cat.opteams.blackjack.domain.model.valueobject.Rank;
import cat.opteams.blackjack.domain.model.valueobject.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class Deck {
    private final List<Card> cards;
    private final int currentIndex;

    public Deck() {
        this(System.currentTimeMillis());
    }

    public Deck(long seed) {
        this.cards = initializeAndShuffle(seed);
        this.currentIndex = 0;
    }

    private Deck(List<Card> cards, int currentIndex) {
        this.cards = List.copyOf(cards);  // Còpia defensiva
        this.currentIndex = currentIndex;
    }

    private List<Card> initializeAndShuffle(long seed) {
        List<Card> newCards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                newCards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(newCards, new Random(seed));
        return List.copyOf(newCards);  // Immutable
    }

    public DrawResult draw() {
        if (currentIndex >= cards.size()) {
            throw new IllegalStateException("No more cards in deck");
        }
        Card drawnCard = cards.get(currentIndex);
        Deck remainingDeck = new Deck(cards, currentIndex + 1);
        return new DrawResult(drawnCard, remainingDeck);
    }

    public int remainingCards() {
        return cards.size() - currentIndex;
    }

    public boolean isEmpty() {
        return currentIndex >= cards.size();
    }

    public List<Card> getAllCards() {
        return cards;
    }
}
