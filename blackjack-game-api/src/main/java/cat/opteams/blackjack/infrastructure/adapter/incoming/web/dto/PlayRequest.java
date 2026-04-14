package cat.opteams.blackjack.infrastructure.adapter.incoming.web.dto;

import jakarta.validation.constraints.NotNull;

public record PlayRequest(
        @NotNull(message = "Action is required")
        Action action
) {
    public enum Action {
        HIT,
        STAND
    }
}
