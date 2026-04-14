package cat.opteams.blackjack.infrastructure.adapter.incoming.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CreateGameRequest(
        @NotBlank(message = "Player name is required")
        @Size(max = 50, message = "Player name cannot exceed 50 characters")
        String playerName,

        @NotNull(message = "Bet is required")
        @DecimalMin(value = "0.01", message = "Bet must be positive")
        BigDecimal bet
) {}
