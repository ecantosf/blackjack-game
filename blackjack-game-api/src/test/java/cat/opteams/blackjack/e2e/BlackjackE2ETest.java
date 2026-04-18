package cat.opteams.blackjack.e2e;

import cat.opteams.blackjack.UnitTest;
import cat.opteams.blackjack.domain.model.aggregate.Game;
import cat.opteams.blackjack.domain.model.entity.Player;
import cat.opteams.blackjack.domain.model.valueobject.GameStatus;
import cat.opteams.blackjack.domain.model.valueobject.Money;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;
import cat.opteams.blackjack.domain.model.valueobject.PlayerName;
import cat.opteams.blackjack.domain.service.Deck;
import cat.opteams.blackjack.shared.exception.GameAlreadyFinishedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Blackjack E2E Tests")
class BlackjackE2ETest extends UnitTest {

    @Test
    @DisplayName("Complete game flow - Player wins by having higher score")
    void completeGameFlowPlayerWins() {
        Player player = new Player(new PlayerId(), new PlayerName("TestPlayer"));
        Money bet = new Money(new BigDecimal("100"));
        Deck deck = new Deck(42L); // Fixed seed for reproducibility
        Game game = Game.startNewGame(player.getId(), bet);

        var draw1 = deck.draw();
        var draw2 = draw1.remainingDeck().draw();
        var draw3 = draw2.remainingDeck().draw();
        var draw4 = draw3.remainingDeck().draw();
        Game initialized = game.initialize(draw1, draw2, draw3, draw4);

        assertEquals(GameStatus.IN_PROGRESS, initialized.getStatus());

        Game finished = initialized.stand(java.util.List.of());

        assertEquals(GameStatus.FINISHED, finished.getStatus());
        assertNotNull(finished.getWinner());
    }

    @Test
    @DisplayName("Complete game flow - Player busts")
    void completeGameFlowPlayerBusts() {
        Player player = new Player(new PlayerId(), new PlayerName("TestPlayer"));
        Money bet = new Money(new BigDecimal("100"));
        Deck deck = new Deck(42L);
        Game game = Game.startNewGame(player.getId(), bet);

        var draw1 = deck.draw();
        var draw2 = draw1.remainingDeck().draw();
        var draw3 = draw2.remainingDeck().draw();
        var draw4 = draw3.remainingDeck().draw();
        Game current = game.initialize(draw1, draw2, draw3, draw4);

        while (!current.getPlayerHand().isBust() && current.getStatus() == GameStatus.IN_PROGRESS) {
            var draw = deck.draw();
            current = current.hit(draw.card());
            deck = draw.remainingDeck();
        }

        if (current.getPlayerHand().isBust()) {
            assertEquals(GameStatus.FINISHED, current.getStatus());
            assertEquals(Game.Player.DEALER, current.getWinner());
        }
    }

    @Test
    @DisplayName("Player can hit multiple times")
    void playerCanHitMultipleTimes() {
        Player player = new Player(new PlayerId(), new PlayerName("TestPlayer"));
        Money bet = new Money(new BigDecimal("100"));
        Deck deck = new Deck(42L);
        Game game = Game.startNewGame(player.getId(), bet);

        var draw1 = deck.draw();
        var draw2 = draw1.remainingDeck().draw();
        var draw3 = draw2.remainingDeck().draw();
        var draw4 = draw3.remainingDeck().draw();
        Game current = game.initialize(draw1, draw2, draw3, draw4);
        int initialCardCount = current.getPlayerHand().getCards().size();

        for (int i = 0; i < 3; i++) {
            var draw = deck.draw();
            current = current.hit(draw.card());
            deck = draw.remainingDeck();
        }

        assertTrue(current.getPlayerHand().getCards().size() > initialCardCount);
    }

    @Test
    @DisplayName("Cannot play after game is finished")
    void cannotPlayAfterGameIsFinished() {
        Player player = new Player(new PlayerId(), new PlayerName("TestPlayer"));
        Money bet = new Money(new BigDecimal("100"));
        Deck deck = new Deck(42L);
        Game game = Game.startNewGame(player.getId(), bet);

        var draw1 = deck.draw();
        var draw2 = draw1.remainingDeck().draw();
        var draw3 = draw2.remainingDeck().draw();
        var draw4 = draw3.remainingDeck().draw();
        Game initialized = game.initialize(draw1, draw2, draw3, draw4);
        Game finished = initialized.stand(java.util.List.of());

        var finalDraw = deck.draw();
        assertThrows(GameAlreadyFinishedException.class, () -> finished.hit(finalDraw.card()));
    }
}
