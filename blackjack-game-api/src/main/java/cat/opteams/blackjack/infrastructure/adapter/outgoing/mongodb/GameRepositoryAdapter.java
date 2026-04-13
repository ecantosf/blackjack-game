package cat.opteams.blackjack.infrastructure.adapter.outgoing.mongodb;

import cat.opteams.blackjack.domain.model.aggregate.Game;
import cat.opteams.blackjack.domain.model.valueobject.GameId;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;
import cat.opteams.blackjack.domain.port.outgoing.GameRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameRepositoryAdapter implements GameRepositoryPort {

    private final GameReactiveRepository reactiveRepository;
    private final GameDocumentMapper mapper;

    @Override
    public Mono<Game> save(Game game) {
        if (game == null) {
            return Mono.error(new IllegalArgumentException("Cannot save null game"));
        }

        log.debug("Saving game with id: {}", game.getId().getValue());

        return Mono.fromCallable(() -> mapper.toDocument(game))
                .flatMap(reactiveRepository::save)
                .map(mapper::toDomain)
                .doOnSuccess(saved -> log.debug("Game saved successfully: {}", saved.getId().getValue()))
                .doOnError(error -> log.error("Error saving game: {}", error.getMessage()));
    }

    @Override
    public Mono<Game> findById(GameId id) {
        if (id == null) {
            return Mono.error(new IllegalArgumentException("GameId cannot be null"));
        }

        String idAsString = id.getValue().toString();
        log.debug("Finding game by id: {}", idAsString);
        
        return reactiveRepository.findById(idAsString)
                .flatMap(document -> {
                    try {
                        return Mono.just(mapper.toDomain(document));
                    } catch (Exception e) {
                        log.error("Failed to map document to Game: {}", e.getMessage(), e);
                        return Mono.error(new IllegalStateException("Failed to map document to Game", e));
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.debug("Game not found: {}", idAsString);
                    return Mono.empty();
                }))
                .doOnError(error -> log.error("Error finding game: {}", error.getMessage()));
    }

    @Override
    public Mono<Boolean> existsById(GameId id) {
        if (id == null) {
            return Mono.error(new IllegalArgumentException("GameId cannot be null"));
        }

        String idAsString = id.getValue().toString();
        log.debug("Checking existence of game: {}", idAsString);

        return reactiveRepository.existsById(idAsString)
                .doOnSuccess(exists -> log.debug("Game exists: {} -> {}", idAsString, exists))
                .doOnError(error -> log.error("Error checking game existence: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteById(GameId id) {
        if (id == null) {
            return Mono.error(new IllegalArgumentException("GameId cannot be null"));
        }

        String idAsString = id.getValue().toString();
        log.debug("Deleting game by id: {}", idAsString);

        return reactiveRepository.deleteById(idAsString)
                .doOnSuccess(v -> log.debug("Game deleted successfully: {}", idAsString))
                .doOnError(error -> log.error("Error deleting game: {}", error.getMessage()));
    }

    @Override
    public Flux<Game> findAllByPlayerId(PlayerId playerId) {
        if (playerId == null) {
            return Flux.error(new IllegalArgumentException("PlayerId cannot be null"));
        }

        String playerIdAsString = playerId.getValue().toString();
        log.debug("Finding all games for player: {}", playerIdAsString);

        return reactiveRepository.findByPlayerId(playerIdAsString)
                .map(document -> {
                    try {
                        return mapper.toDomain(document);
                    } catch (Exception e) {
                        log.error("Failed to map document to Game: {}", e.getMessage(), e);
                        throw new IllegalStateException("Failed to map document to Game", e);
                    }
                })
                .onErrorResume(error -> {
                    log.error("Error finding games by player: {}", error.getMessage());
                    return Flux.empty();
                });
    }
}
