package cat.opteams.blackjack.domain.event;

import cat.opteams.blackjack.domain.model.valueobject.Card;
import cat.opteams.blackjack.domain.model.valueobject.GameId;

public class CardDealtEvent extends DomainEvent {
    private final GameId gameId;
    private final Card card;
    private final Target target;

    public enum Target {
        PLAYER,
        DEALER
    }

    public CardDealtEvent(GameId gameId, Card card, Target target) {
        super("CARD_DEALT");
        this.gameId = gameId;
        this.card = card;
        this.target = target;
    }

    public GameId getGameId() {
        return gameId;
    }

    public Card getCard() {
        return card;
    }

    public Target getTarget() {
        return target;
    }
}

