package cat.opteams.blackjack.domain.model.aggregate;

import cat.opteams.blackjack.domain.model.valueobject.GameStatus;
import cat.opteams.blackjack.domain.model.valueobject.Money;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;
import cat.opteams.blackjack.domain.service.Deck;
import cat.opteams.blackjack.domain.service.DrawResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class GameSmokeTest {

    @Test
    void shouldCreateNewGame() {
        PlayerId playerId = new PlayerId();
        Money bet = new Money(BigDecimal.valueOf(100));
        Deck deck = new Deck(42L);  // Seed fixa per a tests reproduïbles

        // Draw 4 cards: 2 for player, 2 for dealer
        DrawResult p1 = deck.draw();
        DrawResult p2 = p1.remainingDeck().draw();
        DrawResult d1 = p2.remainingDeck().draw();
        DrawResult d2 = d1.remainingDeck().draw();

        Game game = Game.startNewGame(playerId, bet)
                .initialize(p1, p2, d1, d2);

        assertNotNull(game.getId());
        assertEquals(playerId, game.getPlayerId());
        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
        assertEquals(2, game.getPlayerHand().getCardCount());
        assertEquals(2, game.getDealerHand().getCardCount());
    }

    @Test
    void shouldPlayerBust() {
        PlayerId playerId = new PlayerId();
        Money bet = new Money(BigDecimal.valueOf(100));
        Deck deck = new Deck(42L);

        // Initialize game
        DrawResult p1 = deck.draw();
        DrawResult p2 = p1.remainingDeck().draw();
        DrawResult d1 = p2.remainingDeck().draw();
        DrawResult d2 = d1.remainingDeck().draw();

        Game game = Game.startNewGame(playerId, bet)
                .initialize(p1, p2, d1, d2);

        Deck currentDeck = d2.remainingDeck();

        // Hit until bust or 21
        while (!game.getPlayerHand().isBust() && game.getPlayerHand().calculateValue() < 21) {
            DrawResult draw = currentDeck.draw();
            game = game.hit(draw.card());
            currentDeck = draw.remainingDeck();
        }

        if (game.getPlayerHand().isBust()) {
            assertTrue(game.isFinished());
            assertEquals(Game.Player.DEALER, game.getWinner());
        }
    }
}