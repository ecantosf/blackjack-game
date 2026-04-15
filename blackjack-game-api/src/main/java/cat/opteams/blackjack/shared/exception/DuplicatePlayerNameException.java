package cat.opteams.blackjack.shared.exception;

public class DuplicatePlayerNameException extends ConflictException {

    private final String playerName;

    public DuplicatePlayerNameException(String playerName) {
        super("Player name already exists: " + playerName, "DUPLICATE_PLAYER_NAME");
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
