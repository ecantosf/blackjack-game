package cat.opteams.blackjack.domain.service;

import cat.opteams.blackjack.UnitTest;
import cat.opteams.blackjack.domain.model.valueobject.Card;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Deck Service Tests")
class DeckTest extends UnitTest {

    @Test
    @DisplayName("Should create deck with 52 cards")
    void shouldCreateDeckWith52Cards() {
        Deck deck = new Deck();

        assertNotNull(deck);
        assertEquals(52, deck.remainingCards());
    }

    @Test
    @DisplayName("Should draw card and return remaining deck")
    void shouldDrawCardAndReturnRemainingDeck() {
        Deck deck = new Deck(42L); // Fixed seed for reproducibility
        int initialSize = deck.remainingCards();

        DrawResult result = deck.draw();

        assertNotNull(result.card());
        assertNotNull(result.remainingDeck());
        assertEquals(initialSize - 1, result.remainingDeck().remainingCards());
        assertEquals(initialSize, deck.remainingCards());
    }

    @Test
    @DisplayName("Should throw exception when deck is empty")
    void shouldThrowExceptionWhenDeckIsEmpty() {
        Deck deck = new Deck();

        for (int i = 0; i < 52; i++) {
            DrawResult result = deck.draw();
            deck = result.remainingDeck();
        }

        assertTrue(deck.isEmpty());

        Deck finalDeck = deck;
        assertThrows(IllegalStateException.class, finalDeck::draw);
    }

    @Test
    @DisplayName("Should contain all suits and ranks")
    void shouldContainAllSuitsAndRanks() {
        Deck deck = new Deck(42L);
        int[] suitCounts = new int[4];
        int[] rankCounts = new int[13];

        for (int i = 0; i < 52; i++) {
            DrawResult result = deck.draw();
            Card card = result.card();
            suitCounts[card.suit().ordinal()]++;
            rankCounts[card.rank().ordinal()]++;
            deck = result.remainingDeck();
        }

        for (int count : suitCounts) {
            assertEquals(13, count); // Each suit has 13 cards
        }
        for (int count : rankCounts) {
            assertEquals(4, count); // Each rank appears 4 times
        }
    }

    @Test
    @DisplayName("Should detect when deck is empty")
    void shouldDetectWhenDeckIsEmpty() {
        Deck deck = new Deck();
        assertFalse(deck.isEmpty());

        for (int i = 0; i < 52; i++) {
            DrawResult result = deck.draw();
            deck = result.remainingDeck();
        }

        assertTrue(deck.isEmpty());
    }
}
