package cat.opteams.blackjack.application.handler.query;

import cat.opteams.blackjack.application.mapper.RankingResponseMapper;
import cat.opteams.blackjack.application.mapper.RankingEntryResponse;
import cat.opteams.blackjack.application.query.GetRankingQuery;
import cat.opteams.blackjack.domain.port.outgoing.PlayerRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetRankingQueryHandler {

    private final PlayerRepositoryPort playerRepository;
    private final RankingResponseMapper responseMapper;

    public Flux<RankingEntryResponse> handle(GetRankingQuery query) {
        log.debug("Handling GetRankingQuery with limit: {}", query.limit());

        int limit = query.limit() != null ? query.limit() : 10;

        return playerRepository.findAllOrderByTotalPointsDesc(limit)
                .collectList()
                .map(responseMapper::toResponseList)
                .flatMapMany(Flux::fromIterable)
                .doOnComplete(() -> log.debug("Ranking retrieved successfully"))
                .doOnError(error -> log.error("Error getting ranking: {}", error.getMessage()));
    }
}
