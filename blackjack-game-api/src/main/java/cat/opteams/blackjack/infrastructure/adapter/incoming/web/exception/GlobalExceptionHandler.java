package cat.opteams.blackjack.infrastructure.adapter.incoming.web.exception;

import cat.opteams.blackjack.infrastructure.filter.CorrelationIdFilter;
import cat.opteams.blackjack.shared.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        log.info("GlobalExceptionHandler initialized!");  // ← AFEGIR AIXÒ
    }

    @ExceptionHandler(GameNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGameNotFound(
            GameNotFoundException ex,
            ServerWebExchange exchange,
            ContextView contextView
    ) {
        String correlationId = contextView.getOrDefault(CorrelationIdFilter.CORRELATION_ID_KEY, "N/A");
        log.warn("[{}] Game not found: {}", correlationId, ex.getMessage());

        String path = exchange.getRequest().getPath().value();
        ErrorResponse response = ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                "Game Not Found",
                ex.getMessage(),
                path,
                "GAME_NOT_FOUND",
                correlationId
        );

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(response));
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handlePlayerNotFound(
            PlayerNotFoundException ex,
            ServerWebExchange exchange,
            ContextView contextView
    ) {
        String correlationId = contextView.getOrDefault(CorrelationIdFilter.CORRELATION_ID_KEY, "N/A");
        log.warn("[{}] Player not found: {}", correlationId, ex.getMessage());

        String path = exchange.getRequest().getPath().value();
        ErrorResponse response = ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                "Player Not Found",
                ex.getMessage(),
                path,
                "PLAYER_NOT_FOUND",
                correlationId
        );

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(response));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleResourceNotFound(
            ResourceNotFoundException ex,
            ServerWebExchange exchange,
            ContextView contextView
    ) {
        String correlationId = contextView.getOrDefault(CorrelationIdFilter.CORRELATION_ID_KEY, "N/A");
        log.warn("[{}] Resource not found: {}", correlationId, ex.getMessage());

        String path = exchange.getRequest().getPath().value();
        ErrorResponse response = ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                ex.getMessage(),
                path,
                ex.getErrorCode(),
                correlationId
        );

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(response));
    }

    @ExceptionHandler(DomainException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleDomainException(
            DomainException ex,
            ServerWebExchange exchange,
            ContextView contextView
    ) {
        String correlationId = contextView.getOrDefault(CorrelationIdFilter.CORRELATION_ID_KEY, "N/A");
        HttpStatus status = HttpStatus.valueOf(ex.getHttpStatus());

        if (status.is4xxClientError()) {
            log.warn("[{}] Client error [{}]: {}", correlationId, ex.getErrorCode(), ex.getMessage());
        } else {
            log.error("[{}] Server error [{}]: {}", correlationId, ex.getErrorCode(), ex.getMessage(), ex);
        }

        String path = exchange.getRequest().getPath().value();
        ErrorResponse response = ErrorResponse.of(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                path,
                ex.getErrorCode(),
                correlationId
        );

        return Mono.just(ResponseEntity.status(status).body(response));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ValidationErrorResponse>> handleValidationErrors(
            WebExchangeBindException ex,
            ServerWebExchange exchange,
            ContextView contextView
    ) {
        String correlationId = contextView.getOrDefault(CorrelationIdFilter.CORRELATION_ID_KEY, "N/A");
        log.warn("[{}] Validation error: {}", correlationId, ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error instanceof FieldError
                    ? ((FieldError) error).getField()
                    : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String path = exchange.getRequest().getPath().value();
        ValidationErrorResponse response = ValidationErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                errors,
                path
        );

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIllegalArgument(
            IllegalArgumentException ex,
            ServerWebExchange exchange,
            ContextView contextView
    ) {
        String correlationId = contextView.getOrDefault(CorrelationIdFilter.CORRELATION_ID_KEY, "N/A");
        log.warn("[{}] Invalid argument: {}", correlationId, ex.getMessage());

        String path = exchange.getRequest().getPath().value();
        ErrorResponse response = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Request",
                ex.getMessage(),
                path,
                null,
                correlationId
        );

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(
            Exception ex,
            ServerWebExchange exchange,
            ContextView contextView
    ) {
        String correlationId = contextView.getOrDefault(CorrelationIdFilter.CORRELATION_ID_KEY, "N/A");
        log.error("[{}] Unexpected error: {}", correlationId, ex.getMessage(), ex);

        String path = exchange.getRequest().getPath().value();
        ErrorResponse response = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                path,
                null,
                correlationId
        );

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
    }
}
