package cat.opteams.blackjack.shared.exception;

public class PlayerNotFoundException extends ResourceNotFoundException {

    private final String playerId;

    public PlayerNotFoundException(String playerId) {
        super("Player not found with id: " + playerId, "PLAYER_NOT_FOUND");
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }
}
