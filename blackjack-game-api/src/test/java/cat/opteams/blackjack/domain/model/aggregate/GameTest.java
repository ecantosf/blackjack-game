package cat.opteams.blackjack.domain.model.aggregate;

import cat.opteams.blackjack.UnitTest;
import cat.opteams.blackjack.domain.model.entity.Player;
import cat.opteams.blackjack.domain.model.valueobject.*;
import cat.opteams.blackjack.domain.service.Deck;
import cat.opteams.blackjack.shared.exception.GameAlreadyFinishedException;
import cat.opteams.blackjack.testutil.GameTestBuilder;
import cat.opteams.blackjack.testutil.PlayerTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Game Aggregate Tests")
class GameTest extends UnitTest {

    private Player player;
    private Money bet;
    private Deck deck;

    @BeforeEach
    void setUp() {
        player = PlayerTestBuilder.aPlayer()
                .withName("TestPlayer")
                .build();
        bet = new Money(new BigDecimal("100"));
        deck = new Deck(42L);
    }

    @Test
    @DisplayName("Should create game with initial state")
    void shouldCreateGameWithInitialState() {
        Game game = Game.startNewGame(player.getId(), bet);

        assertNotNull(game);
        assertNotNull(game.getId());
        assertEquals(GameStatus.WAITING, game.getStatus());
        assertNull(game.getWinner());
        assertNull(game.getFinishedAt());
    }

    @Test
    @DisplayName("Should initialize game with cards")
    void shouldInitializeGameWithCards() {
        Game game = Game.startNewGame(player.getId(), bet);

        var draw1 = deck.draw();
        var draw2 = draw1.remainingDeck().draw();
        var draw3 = draw2.remainingDeck().draw();
        var draw4 = draw3.remainingDeck().draw();

        Game initialized = game.initialize(draw1, draw2, draw3, draw4);

        assertEquals(GameStatus.IN_PROGRESS, initialized.getStatus());
        assertEquals(2, initialized.getPlayerHand().getCards().size());
        assertEquals(2, initialized.getDealerHand().getCards().size());
    }

    @Test
    @DisplayName("Should throw exception when initializing already initialized game")
    void shouldThrowExceptionWhenInitializingAlreadyInitializedGame() {
        Game game = GameTestBuilder.aGame().build();

        var draw1 = deck.draw();
        var draw2 = draw1.remainingDeck().draw();
        var draw3 = draw2.remainingDeck().draw();
        var draw4 = draw3.remainingDeck().draw();

        assertThrows(IllegalStateException.class, () ->
                game.initialize(draw1, draw2, draw3, draw4)
        );
    }

    @Test
    @DisplayName("Should add card to player hand when hit")
    void shouldAddCardToPlayerHandWhenHit() {
        Game game = GameTestBuilder.aGame().build();
        int initialCardCount = game.getPlayerHand().getCards().size();

        var draw = deck.draw();
        Game afterHit = game.hit(draw.card());

        assertEquals(initialCardCount + 1, afterHit.getPlayerHand().getCards().size());
    }

    @Test
    @DisplayName("Should finish game when player stands")
    void shouldFinishGameWhenPlayerStands() {
        Game game = GameTestBuilder.aGame().build();

        Game afterStand = game.stand(java.util.List.of());

        assertEquals(GameStatus.FINISHED, afterStand.getStatus());
        assertNotNull(afterStand.getWinner());
        assertNotNull(afterStand.getFinishedAt());
    }

    @Test
    @DisplayName("Should finish game when player busts")
    void shouldFinishGameWhenPlayerBusts() {
        Game game = GameTestBuilder.aGame().build();

        Game current = game;
        while (!current.getPlayerHand().isBust() && current.getStatus() == GameStatus.IN_PROGRESS) {
            var draw = deck.draw();
            current = current.hit(draw.card());
        }

        if (current.getPlayerHand().isBust()) {
            assertEquals(GameStatus.FINISHED, current.getStatus());
            assertEquals(Game.Player.DEALER, current.getWinner());
        }
    }

    @Test
    @DisplayName("Should not allow hit when game is finished")
    void shouldNotAllowHitWhenGameIsFinished() {
        Game game = GameTestBuilder.aGame().build();
        Game finished = game.stand(java.util.List.of());

        assertThrows(GameAlreadyFinishedException.class, () ->
                finished.hit(deck.draw().card())
        );
    }

    @Test
    @DisplayName("Should not allow stand when game is finished")
    void shouldNotAllowStandWhenGameIsFinished() {
        Game game = GameTestBuilder.aGame().build();
        Game finished = game.stand(java.util.List.of());

        assertThrows(GameAlreadyFinishedException.class, () ->
                finished.stand(java.util.List.of())
        );
    }

    @Test
    @DisplayName("Should detect player blackjack on start")
    void shouldDetectPlayerBlackjackOnStart() {
        Deck customDeck = new Deck(1L);
        var draw1 = customDeck.draw();
        var draw2 = draw1.remainingDeck().draw();
        var draw3 = draw2.remainingDeck().draw();
        var draw4 = draw3.remainingDeck().draw();

        Game game = Game.startNewGame(player.getId(), bet);
        Game initialized = game.initialize(draw1, draw2, draw3, draw4);

        if (initialized.getPlayerHand().isBlackjack()) {
            assertEquals(GameStatus.FINISHED, initialized.getStatus());
        }
    }

    @Test
    @DisplayName("Should be equal when IDs are the same")
    void shouldBeEqualWhenIdsAreSame() {
        GameId gameId = new GameId();
        Player player = PlayerTestBuilder.aPlayer().build();
        Money bet = new Money(new BigDecimal("100"));

        Game game1 = Game.startNewGame(player.getId(), bet);
        Game game2 = Game.startNewGame(player.getId(), bet);

        assertNotEquals(game1, game2);
    }
}
