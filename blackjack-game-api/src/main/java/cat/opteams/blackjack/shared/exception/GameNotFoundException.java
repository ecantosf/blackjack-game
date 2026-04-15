package cat.opteams.blackjack.shared.exception;

public class GameNotFoundException extends ResourceNotFoundException {

    private final String gameId;

    public GameNotFoundException(String gameId) {
        super("Game not found with id: " + gameId, "GAME_NOT_FOUND");
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }
}
