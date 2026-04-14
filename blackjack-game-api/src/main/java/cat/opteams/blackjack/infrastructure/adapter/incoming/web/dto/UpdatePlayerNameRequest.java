package cat.opteams.blackjack.infrastructure.adapter.incoming.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePlayerNameRequest(
        @NotBlank(message = "New name is required")
        @Size(max = 50, message = "Name cannot exceed 50 characters")
        String newName
) {}
