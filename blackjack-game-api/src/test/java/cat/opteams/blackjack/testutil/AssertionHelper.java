package cat.opteams.blackjack.testutil;

import cat.opteams.blackjack.domain.model.aggregate.Game;
import cat.opteams.blackjack.domain.model.valueobject.GameStatus;
import cat.opteams.blackjack.domain.model.valueobject.Hand;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionHelper {

    public static void assertGameInProgress(Game game) {
        assertNotNull(game);
        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
        assertNull(game.getWinner());
        assertNull(game.getFinishedAt());
    }

    public static void assertGameFinished(Game game) {
        assertNotNull(game);
        assertEquals(GameStatus.FINISHED, game.getStatus());
        assertNotNull(game.getWinner());
        assertNotNull(game.getFinishedAt());
    }

    public static void assertPlayerWon(Game game) {
        assertGameFinished(game);
        assertEquals(Game.Player.PLAYER, game.getWinner());
    }

    public static void assertDealerWon(Game game) {
        assertGameFinished(game);
        assertEquals(Game.Player.DEALER, game.getWinner());
    }

    public static void assertTie(Game game) {
        assertGameFinished(game);
        assertEquals(Game.Player.TIE, game.getWinner());
    }

    public static void assertHandHasBlackjack(Hand hand) {
        assertNotNull(hand);
        assertTrue(hand.isBlackjack(), "Expected hand to be blackjack");
        assertEquals(21, hand.calculateValue());
        assertEquals(2, hand.getCards().size());
    }

    public static void assertHandIsBust(Hand hand) {
        assertNotNull(hand);
        assertTrue(hand.isBust(), "Expected hand to be bust");
        assertTrue(hand.calculateValue() > 21);
    }

    public static void assertHandValue(Hand hand, int expectedValue) {
        assertNotNull(hand);
        assertEquals(expectedValue, hand.calculateValue(),
                "Expected hand value to be " + expectedValue + " but was " + hand.calculateValue());
    }

    public static void assertHandHasCards(Hand hand, int expectedCardCount) {
        assertNotNull(hand);
        assertEquals(expectedCardCount, hand.getCards().size(),
                "Expected " + expectedCardCount + " cards but found " + hand.getCards().size());
    }
}