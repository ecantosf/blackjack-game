package cat.opteams.blackjack.shared.exception;

public abstract class BusinessRuleException extends DomainException {

    protected BusinessRuleException(String message, String errorCode) {
        super(message, errorCode, 400);
    }
}
