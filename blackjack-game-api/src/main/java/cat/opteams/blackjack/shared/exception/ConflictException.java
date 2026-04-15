package cat.opteams.blackjack.shared.exception;

public abstract class ConflictException extends DomainException {

    protected ConflictException(String message, String errorCode) {
        super(message, errorCode, 409);
    }
}
