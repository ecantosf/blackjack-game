package cat.opteams.blackjack.application.handler.command;

import cat.opteams.blackjack.application.command.DeleteGameCommand;
import cat.opteams.blackjack.domain.model.valueobject.GameId;
import cat.opteams.blackjack.domain.port.outgoing.GameRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteGameHandler {

    private final GameRepositoryPort gameRepository;

    public Mono<Void> handle(DeleteGameCommand command) {
        log.debug("Handling DeleteGameCommand for game: {}", command.gameId());

        GameId gameId = new GameId(command.gameId());

        return gameRepository.existsById(gameId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new IllegalArgumentException("Game not found: " + command.gameId()));
                    }
                    return gameRepository.deleteById(gameId);
                })
                .doOnSuccess(v -> log.info("Game deleted successfully: {}", command.gameId()))
                .doOnError(error -> log.error("Error deleting game: {}", error.getMessage()));
    }
}
