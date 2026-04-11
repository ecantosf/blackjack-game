package cat.opteams.blackjack.domain.event;

import cat.opteams.blackjack.domain.model.aggregate.Game;
import cat.opteams.blackjack.domain.model.valueobject.GameId;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;

public class GameFinishedEvent extends DomainEvent {
    private final GameId gameId;
    private final PlayerId playerId;
    private final Game.Player winner;
    private final int pointsWon;

    public GameFinishedEvent(GameId gameId, PlayerId playerId, Game.Player winner, int pointsWon) {
        super("GAME_FINISHED");
        this.gameId = gameId;
        this.playerId = playerId;
        this.winner = winner;
        this.pointsWon = pointsWon;
    }

    public GameId getGameId() {
        return gameId;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }

    public Game.Player getWinner() {
        return winner;
    }

    public int getPointsWon() {
        return pointsWon;
    }
}
