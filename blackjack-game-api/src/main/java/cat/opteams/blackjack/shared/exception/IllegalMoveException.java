package cat.opteams.blackjack.shared.exception;

public class IllegalMoveException extends BusinessRuleException {

    public IllegalMoveException(String message) {
        super(message, "ILLEGAL_MOVE");
    }
}