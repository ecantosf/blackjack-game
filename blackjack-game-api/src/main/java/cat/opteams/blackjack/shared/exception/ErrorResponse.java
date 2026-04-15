package cat.opteams.blackjack.shared.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        String errorCode,
        String correlationId
) {

    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path, null, null);
    }

    public static ErrorResponse of(int status, String error, String message, String path, String errorCode) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path, errorCode, null);
    }

    public static ErrorResponse of(int status, String error, String message, String path, String errorCode, String correlationId) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path, errorCode, correlationId);
    }
}
