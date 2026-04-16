package cat.opteams.blackjack.testutil;

import cat.opteams.blackjack.domain.model.aggregate.Game;
import cat.opteams.blackjack.domain.model.entity.Player;
import cat.opteams.blackjack.domain.model.valueobject.Money;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;
import cat.opteams.blackjack.domain.model.valueobject.PlayerName;

import java.math.BigDecimal;

public class TestDataFactory {

    public static Player createDefaultPlayer() {
        return new Player(new PlayerId(), new PlayerName("TestPlayer"));
    }

    public static Player createPlayerWithName(String name) {
        return new Player(new PlayerId(), new PlayerName(name));
    }

    public static Player createPlayerWithIdAndName(PlayerId id, String name) {
        return new Player(id, new PlayerName(name));
    }

    public static Game createDefaultGame() {
        Player player = createDefaultPlayer();
        Money bet = new Money(new BigDecimal("100"));
        return Game.startNewGame(player.getId(), bet);
    }

    public static Game createGameWithPlayer(Player player, BigDecimal betAmount) {
        Money bet = new Money(betAmount);
        return Game.startNewGame(player.getId(), bet);
    }
}
