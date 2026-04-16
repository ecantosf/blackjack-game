package cat.opteams.blackjack.domain.model.valueobject;

import cat.opteams.blackjack.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Card Value Object Tests")
class CardTest extends UnitTest {

    @Test
    @DisplayName("Should create card with suit and rank")
    void shouldCreateCardWithSuitAndRank() {
        Card card = new Card(Suit.HEARTS, Rank.ACE);

        assertEquals(Suit.HEARTS, card.suit());
        assertEquals(Rank.ACE, card.rank());
    }

    @Test
    @DisplayName("Should throw exception when suit is null")
    void shouldThrowExceptionWhenSuitIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Card(null, Rank.ACE)
        );
    }

    @Test
    @DisplayName("Should throw exception when rank is null")
    void shouldThrowExceptionWhenRankIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Card(Suit.HEARTS, null)
        );
    }

    @ParameterizedTest
    @EnumSource(Rank.class)
    @DisplayName("Should get correct value for each rank")
    void shouldGetCorrectValueForEachRank(Rank rank) {
        Card card = new Card(Suit.HEARTS, rank);
        int value = card.getValue();

        assertTrue(value >= 1 && value <= 11);

        switch (rank) {
            case ACE -> assertEquals(11, value);
            case TWO -> assertEquals(2, value);
            case THREE -> assertEquals(3, value);
            case FOUR -> assertEquals(4, value);
            case FIVE -> assertEquals(5, value);
            case SIX -> assertEquals(6, value);
            case SEVEN -> assertEquals(7, value);
            case EIGHT -> assertEquals(8, value);
            case NINE -> assertEquals(9, value);
            case TEN, JACK, QUEEN, KING -> assertEquals(10, value);
        }
    }

    @Test
    @DisplayName("Should detect Ace correctly")
    void shouldDetectAceCorrectly() {
        Card ace = new Card(Suit.HEARTS, Rank.ACE);
        Card king = new Card(Suit.SPADES, Rank.KING);

        assertTrue(ace.isAce());
        assertFalse(king.isAce());
    }

    @Test
    @DisplayName("Should be equal when suit and rank are the same")
    void shouldBeEqualWhenSuitAndRankAreSame() {
        Card card1 = new Card(Suit.HEARTS, Rank.ACE);
        Card card2 = new Card(Suit.HEARTS, Rank.ACE);

        assertEquals(card1, card2);
        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when suits are different")
    void shouldNotBeEqualWhenSuitsAreDifferent() {
        Card card1 = new Card(Suit.HEARTS, Rank.ACE);
        Card card2 = new Card(Suit.SPADES, Rank.ACE);

        assertNotEquals(card1, card2);
    }
}
