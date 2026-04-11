package cat.opteams.blackjack.domain.event;

import cat.opteams.blackjack.domain.model.valueobject.GameId;
import cat.opteams.blackjack.domain.model.valueobject.Money;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;

public class GameCreatedEvent extends DomainEvent {
    private final GameId gameId;
    private final PlayerId playerId;
    private final Money bet;

    public GameCreatedEvent(GameId gameId, PlayerId playerId, Money bet) {
        super("GAME_CREATED");
        this.gameId = gameId;
        this.playerId = playerId;
        this.bet = bet;
    }

    public GameId getGameId() {
        return gameId;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }

    public Money getBet() {
        return bet;
    }
}
