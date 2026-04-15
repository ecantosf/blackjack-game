package cat.opteams.blackjack.shared.exception;

public abstract class ResourceNotFoundException extends DomainException {

    protected ResourceNotFoundException(String message, String errorCode) {
        super(message, errorCode, 404);
    }
}
