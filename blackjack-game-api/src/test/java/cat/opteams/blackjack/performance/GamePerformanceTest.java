package cat.opteams.blackjack.performance;

import cat.opteams.blackjack.UnitTest;
import cat.opteams.blackjack.domain.model.aggregate.Game;
import cat.opteams.blackjack.domain.model.entity.Player;
import cat.opteams.blackjack.domain.model.valueobject.Money;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;
import cat.opteams.blackjack.domain.model.valueobject.PlayerName;
import cat.opteams.blackjack.domain.service.Deck;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Game Performance Tests")
class GamePerformanceTest extends UnitTest {

    @Test
    @DisplayName("Should complete 100 games in less than 1 second")
    void shouldComplete100GamesInLessThan1Second() {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            Player player = new Player(new PlayerId(), new PlayerName("PerfTest_" + i));
            Money bet = new Money(new BigDecimal("100"));
            Deck deck = new Deck();
            Game game = Game.startNewGame(player.getId(), bet);

            var draw1 = deck.draw();
            var draw2 = draw1.remainingDeck().draw();
            var draw3 = draw2.remainingDeck().draw();
            var draw4 = draw3.remainingDeck().draw();
            Game current = game.initialize(draw1, draw2, draw3, draw4);
            Deck currentDeck = draw4.remainingDeck();

            while (!current.getPlayerHand().isBust() && current.getPlayerHand().calculateValue() < 17) {
                var draw = currentDeck.draw();
                current = current.hit(draw.card());
                currentDeck = draw.remainingDeck();
            }

            if (!current.isFinished()) {
                current = current.stand(java.util.List.of());
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertTrue(duration < 1000,
                "Expected 100 games to complete in less than 1000ms, but took " + duration + "ms");
    }

    @Test
    @DisplayName("Game creation should be fast")
    void gameCreationShouldBeFast() {
        Player player = new Player(new PlayerId(), new PlayerName("TestPlayer"));
        Money bet = new Money(new BigDecimal("100"));
        Deck deck = new Deck();
        long startTime = System.nanoTime();

        Game game = Game.startNewGame(player.getId(), bet);
        var draw1 = deck.draw();
        var draw2 = draw1.remainingDeck().draw();
        var draw3 = draw2.remainingDeck().draw();
        var draw4 = draw3.remainingDeck().draw();
        Game initialized = game.initialize(draw1, draw2, draw3, draw4);

        long endTime = System.nanoTime();
        long durationMicros = (endTime - startTime) / 1000;

        assertTrue(durationMicros < 10000,
                "Expected game creation to take less than 10000 microseconds, but took " + durationMicros + "µs");
    }
}
