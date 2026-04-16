package cat.opteams.blackjack.domain.model.valueobject;

import cat.opteams.blackjack.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Hand Value Object Tests")
class HandTest extends UnitTest {

    @Test
    @DisplayName("Should create empty hand")
    void shouldCreateEmptyHand() {
        Hand hand = new Hand();
        assertNotNull(hand);
        assertTrue(hand.getCards().isEmpty());
        assertEquals(0, hand.calculateValue());
    }

    @Test
    @DisplayName("Should add card to hand")
    void shouldAddCardToHand() {
        Hand hand = new Hand();
        Card card = new Card(Suit.HEARTS, Rank.TEN);

        Hand newHand = hand.addCard(card);

        assertEquals(1, newHand.getCards().size());
        assertEquals(10, newHand.calculateValue());
        assertTrue(hand.getCards().isEmpty()); // Original unchanged (immutable)
    }

    @Test
    @DisplayName("Should calculate hand value correctly")
    void shouldCalculateHandValueCorrectly() {
        Hand hand = new Hand();
        hand = hand.addCard(new Card(Suit.HEARTS, Rank.TEN));
        hand = hand.addCard(new Card(Suit.SPADES, Rank.FIVE));

        assertEquals(15, hand.calculateValue());
    }

    @Test
    @DisplayName("Should detect blackjack")
    void shouldDetectBlackjack() {
        Hand hand = new Hand();
        hand = hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand = hand.addCard(new Card(Suit.SPADES, Rank.KING));

        assertTrue(hand.isBlackjack());
        assertEquals(21, hand.calculateValue());
    }

    @Test
    @DisplayName("Should detect bust")
    void shouldDetectBust() {
        Hand hand = new Hand();
        hand = hand.addCard(new Card(Suit.HEARTS, Rank.TEN));
        hand = hand.addCard(new Card(Suit.SPADES, Rank.TEN));
        hand = hand.addCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        assertTrue(hand.isBust());
        assertTrue(hand.calculateValue() > 21);
    }

    @Test
    @DisplayName("Should handle Ace as 11 or 1")
    void shouldHandleAceCorrectly() {
        Hand hand = new Hand();
        hand = hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand = hand.addCard(new Card(Suit.SPADES, Rank.FIVE));

        assertEquals(16, hand.calculateValue()); // Ace counted as 11

        hand = hand.addCard(new Card(Suit.DIAMONDS, Rank.TEN));
        assertEquals(16, hand.calculateValue()); // Ace now counted as 1
    }

    @Test
    @DisplayName("Should handle multiple Aces")
    void shouldHandleMultipleAces() {
        Hand hand = new Hand();
        hand = hand.addCard(new Card(Suit.HEARTS, Rank.ACE));
        hand = hand.addCard(new Card(Suit.SPADES, Rank.ACE));
        hand = hand.addCard(new Card(Suit.DIAMONDS, Rank.NINE));

        assertEquals(21, hand.calculateValue()); // One Ace as 11, one as 1
    }

    @Test
    @DisplayName("Should detect when can hit")
    void shouldDetectCanHit() {
        Hand hand = new Hand();
        hand = hand.addCard(new Card(Suit.HEARTS, Rank.TEN));
        hand = hand.addCard(new Card(Suit.SPADES, Rank.FIVE));

        assertTrue(hand.canHit()); // 15 points - can hit

        hand = hand.addCard(new Card(Suit.DIAMONDS, Rank.TEN));
        assertFalse(hand.canHit()); // 25 points - bust
    }
}
