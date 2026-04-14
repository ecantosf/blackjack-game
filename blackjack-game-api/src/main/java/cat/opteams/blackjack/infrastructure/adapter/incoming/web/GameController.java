package cat.opteams.blackjack.infrastructure.adapter.incoming.web;

import cat.opteams.blackjack.application.command.CreateGameCommand;
import cat.opteams.blackjack.application.command.DeleteGameCommand;
import cat.opteams.blackjack.application.command.PlayCommand;
import cat.opteams.blackjack.application.handler.command.CreateGameHandler;
import cat.opteams.blackjack.application.handler.command.DeleteGameHandler;
import cat.opteams.blackjack.application.handler.command.PlayHandler;
import cat.opteams.blackjack.application.handler.query.GetGameQueryHandler;
import cat.opteams.blackjack.application.mapper.GameResponse;
import cat.opteams.blackjack.application.query.GetGameQuery;
import cat.opteams.blackjack.infrastructure.adapter.incoming.web.dto.CreateGameRequest;
import cat.opteams.blackjack.infrastructure.adapter.incoming.web.dto.PlayRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final CreateGameHandler createGameHandler;
    private final PlayHandler playHandler;
    private final DeleteGameHandler deleteGameHandler;
    private final GetGameQueryHandler getGameQueryHandler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new Blackjack game",
            description = "Creates a new game for a player with the specified bet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Game created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input (name or bet)")
    })
    public Mono<GameResponse> createGame(@Valid @RequestBody CreateGameRequest request) {
        log.info("POST /games - Creating game for player: {}", request.playerName());

        CreateGameCommand command = new CreateGameCommand(
                request.playerName(),
                request.bet()
        );

        return createGameHandler.handle(command)
                .doOnSuccess(response -> log.info("Game created successfully with id: {}", response.id()))
                .doOnError(error -> log.error("Error creating game: {}", error.getMessage()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get game details",
            description = "Retrieves detailed information about a specific game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game found"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    public Mono<GameResponse> getGame(
            @Parameter(description = "Game ID (UUID format)", required = true)
            @PathVariable
            @Pattern(regexp = "^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$",
                    message = "Invalid UUID format")
            String id
    ) {
        log.info("GET /games/{} - Retrieving game details", id);

        GetGameQuery query = new GetGameQuery(id);

        return getGameQueryHandler.handle(query)
                .doOnSuccess(response -> log.info("Game retrieved successfully: {}", id))
                .doOnError(error -> log.error("Error retrieving game: {}", error.getMessage()));
    }

    @PostMapping("/{id}/play")
    @Operation(summary = "Make a play",
            description = "Performs a HIT or STAND action in an existing game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Play executed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid action or game state"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    public Mono<GameResponse> play(
            @Parameter(description = "Game ID (UUID format)", required = true)
            @PathVariable
            @Pattern(regexp = "^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$",
                    message = "Invalid UUID format")
            String id,
            @Valid @RequestBody PlayRequest request
    ) {
        log.info("POST /games/{}/play - Action: {}", id, request.action());

        PlayCommand.Action action = PlayCommand.Action.valueOf(request.action().name());
        PlayCommand command = new PlayCommand(id, action);

        return playHandler.handle(command)
                .doOnSuccess(response -> log.info("Play executed successfully for game: {}", id))
                .doOnError(error -> log.error("Error executing play: {}", error.getMessage()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a game",
            description = "Permanently deletes a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Game deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    public Mono<Void> deleteGame(
            @Parameter(description = "Game ID (UUID format)", required = true)
            @PathVariable
            @Pattern(regexp = "^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$",
                    message = "Invalid UUID format")
            String id
    ) {
        log.info("DELETE /games/{} - Deleting game", id);

        DeleteGameCommand command = new DeleteGameCommand(id);

        return deleteGameHandler.handle(command)
                .doOnSuccess(v -> log.info("Game deleted successfully: {}", id))
                .doOnError(error -> log.error("Error deleting game: {}", error.getMessage()));
    }
}
