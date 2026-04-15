package cat.opteams.blackjack.shared.exception;

public class GameAlreadyFinishedException extends BusinessRuleException {

    public GameAlreadyFinishedException(String message) {
        super(message, "GAME_ALREADY_FINISHED");
    }
}
