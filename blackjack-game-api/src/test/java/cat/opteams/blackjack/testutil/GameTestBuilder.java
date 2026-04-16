package cat.opteams.blackjack.testutil;

import cat.opteams.blackjack.domain.model.aggregate.Game;
import cat.opteams.blackjack.domain.model.entity.Player;
import cat.opteams.blackjack.domain.model.valueobject.*;
import cat.opteams.blackjack.domain.service.Deck;

import java.math.BigDecimal;

public class GameTestBuilder {

    private Player player;
    private Money bet;
    private Deck deck;

    private GameTestBuilder() {
        this.player = TestDataFactory.createDefaultPlayer();
        this.bet = new Money(new BigDecimal("100"));
        this.deck = new Deck(42L); // Fixed seed for reproducibility
    }

    public static GameTestBuilder aGame() {
        return new GameTestBuilder();
    }

    public GameTestBuilder withPlayer(Player player) {
        this.player = player;
        return this;
    }

    public GameTestBuilder withBet(BigDecimal amount) {
        this.bet = new Money(amount);
        return this;
    }

    public GameTestBuilder withSeed(long seed) {
        this.deck = new Deck(seed);
        return this;
    }

    public Game build() {
        Game game = Game.startNewGame(player.getId(), bet);

        // Draw 4 cards: 2 for player, 2 for dealer
        var draw1 = deck.draw();
        var draw2 = draw1.remainingDeck().draw();
        var draw3 = draw2.remainingDeck().draw();
        var draw4 = draw3.remainingDeck().draw();

        return game.initialize(draw1, draw2, draw3, draw4);
    }

    public Game buildFinished() {
        Game game = build();
        // Draw until game is finished (simulate stand)
        return game.stand(java.util.List.of());
    }
}
