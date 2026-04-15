package cat.opteams.blackjack.shared.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ValidationErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        Map<String, String> validationErrors,
        String path
) {

    public static ValidationErrorResponse of(int status, String error, Map<String, String> validationErrors, String path) {
        return new ValidationErrorResponse(LocalDateTime.now(), status, error, validationErrors, path);
    }
}
