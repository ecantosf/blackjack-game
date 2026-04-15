package cat.opteams.blackjack.shared.exception;

public class InvalidBetException extends BusinessRuleException {

    public InvalidBetException(String message) {
        super(message, "INVALID_BET");
    }
}