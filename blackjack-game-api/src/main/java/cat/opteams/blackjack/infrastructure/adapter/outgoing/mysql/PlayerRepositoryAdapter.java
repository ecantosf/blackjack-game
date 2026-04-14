package cat.opteams.blackjack.infrastructure.adapter.outgoing.mysql;

import cat.opteams.blackjack.domain.model.entity.Player;
import cat.opteams.blackjack.domain.model.valueobject.PlayerId;
import cat.opteams.blackjack.domain.model.valueobject.PlayerName;
import cat.opteams.blackjack.domain.port.outgoing.PlayerRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerRepositoryAdapter implements PlayerRepositoryPort {

    private final PlayerR2dbcRepository r2dbcRepository;
    private final PlayerEntityMapper mapper;

    @Override
    public Mono<Player> save(Player player) {
        if (player == null) {
            return Mono.error(new IllegalArgumentException("Cannot save null player"));
        }

        log.debug("Saving player with id: {}", player.getId().getValue());

        return Mono.fromCallable(() -> mapper.toEntity(player))
                .flatMap(r2dbcRepository::save)
                .map(mapper::toDomain)
                .doOnSuccess(saved -> log.debug("Player saved successfully: {}", saved.getId().getValue()))
                .doOnError(error -> log.error("Error saving player: {}", error.getMessage()));
    }

    @Override
    public Mono<Player> findById(PlayerId id) {
        if (id == null) {
            return Mono.error(new IllegalArgumentException("PlayerId cannot be null"));
        }

        String idAsString = id.getValue().toString();
        log.debug("Finding player by id: {}", idAsString);

        return r2dbcRepository.findById(idAsString)
                .flatMap(entity -> {
                    try {
                        return Mono.just(mapper.toDomain(entity));
                    } catch (Exception e) {
                        log.error("Failed to map entity to Player: {}", e.getMessage(), e);
                        return Mono.error(new IllegalStateException("Failed to map entity to Player", e));
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.debug("Player not found: {}", idAsString);
                    return Mono.empty();
                }))
                .doOnError(error -> log.error("Error finding player: {}", error.getMessage()));
    }

    @Override
    public Mono<Player> findByName(PlayerName name) {
        if (name == null) {
            return Mono.error(new IllegalArgumentException("PlayerName cannot be null"));
        }

        String nameAsString = name.getValue();
        log.debug("Finding player by name: {}", nameAsString);

        return r2dbcRepository.findByName(nameAsString)
                .flatMap(entity -> {
                    try {
                        return Mono.just(mapper.toDomain(entity));
                    } catch (Exception e) {
                        log.error("Failed to map entity to Player: {}", e.getMessage(), e);
                        return Mono.error(new IllegalStateException("Failed to map entity to Player", e));
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.debug("Player not found by name: {}", nameAsString);
                    return Mono.empty();
                }))
                .doOnError(error -> log.error("Error finding player by name: {}", error.getMessage()));
    }

    @Override
    public Mono<Boolean> existsByName(PlayerName name) {
        if (name == null) {
            return Mono.error(new IllegalArgumentException("PlayerName cannot be null"));
        }

        String nameAsString = name.getValue();
        log.debug("Checking existence of player by name: {}", nameAsString);

        return r2dbcRepository.existsByName(nameAsString)
                .doOnSuccess(exists -> log.debug("Player exists by name: {} -> {}", nameAsString, exists))
                .doOnError(error -> log.error("Error checking player existence by name: {}", error.getMessage()));
    }

    @Override
    public Flux<Player> findAllOrderByTotalPointsDesc(int limit) {
        if (limit <= 0) {
            return Flux.error(new IllegalArgumentException("Limit must be positive"));
        }

        log.debug("Finding all players ordered by total points descending, limit: {}", limit);

        return r2dbcRepository.findAllOrderByTotalPointsDesc(limit)
                .flatMap(entity -> {
                    try {
                        return Mono.just(mapper.toDomain(entity));
                    } catch (Exception e) {
                        log.error("Failed to map entity to Player: {}", e.getMessage(), e);
                        return Mono.error(new IllegalStateException("Failed to map entity to Player", e));
                    }
                })
                .doOnError(error -> log.error("Error finding players by ranking: {}", error.getMessage()));
    }
}
