package cat.opteams.blackjack.infrastructure.adapter.incoming.web;

import cat.opteams.blackjack.application.command.UpdatePlayerNameCommand;
import cat.opteams.blackjack.application.handler.command.UpdatePlayerNameHandler;
import cat.opteams.blackjack.application.mapper.RankingEntryResponse;
import cat.opteams.blackjack.infrastructure.adapter.incoming.web.dto.UpdatePlayerNameRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final UpdatePlayerNameHandler updatePlayerNameHandler;

    @PatchMapping("/{playerId}/name")
    @Operation(summary = "Update player name",
            description = "Changes the name of an existing player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Name updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid name"),
            @ApiResponse(responseCode = "404", description = "Player not found"),
            @ApiResponse(responseCode = "409", description = "Name already exists")
    })
    public Mono<RankingEntryResponse> updatePlayerName(
            @Parameter(description = "Player ID (UUID format)", required = true)
            @PathVariable
            @Pattern(regexp = "^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$",
                    message = "Invalid UUID format")
            String playerId,
            @Valid @RequestBody UpdatePlayerNameRequest request
    ) {
        log.info("PATCH /players/{}/name - Updating player name to: {}", playerId, request.newName());

        UpdatePlayerNameCommand command = new UpdatePlayerNameCommand(
                playerId,
                request.newName()
        );

        return updatePlayerNameHandler.handle(command)
                .doOnSuccess(response -> log.info("Player name updated successfully: {}", response.playerId()))
                .doOnError(error -> log.error("Error updating player name: {}", error.getMessage()));
    }
}
