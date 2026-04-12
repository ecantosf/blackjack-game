package cat.opteams.blackjack.application.handler.query;

import cat.opteams.blackjack.application.mapper.GameResponseMapper;
import cat.opteams.blackjack.application.mapper.GameResponse;
import cat.opteams.blackjack.application.query.GetGameQuery;
import cat.opteams.blackjack.domain.model.valueobject.GameId;
import cat.opteams.blackjack.domain.port.outgoing.GameRepositoryPort;
import cat.opteams.blackjack.domain.port.outgoing.PlayerRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetGameQueryHandler {

    private final GameRepositoryPort gameRepository;
    private final PlayerRepositoryPort playerRepository;
    private final GameResponseMapper responseMapper;

    public Mono<GameResponse> handle(GetGameQuery query) {
        log.debug("Handling GetGameQuery for game: {}", query.gameId());

        GameId gameId = new GameId(query.gameId());

        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Game not found: " + query.gameId())))
                .flatMap(game -> playerRepository.findById(game.getPlayerId())
                        .map(player -> responseMapper.toResponse(game, player.getName().getValue())))
                .doOnError(error -> log.error("Error getting game: {}", error.getMessage()));
    }
}
