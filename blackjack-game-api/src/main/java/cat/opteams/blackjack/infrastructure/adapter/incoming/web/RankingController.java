package cat.opteams.blackjack.infrastructure.adapter.incoming.web;

import cat.opteams.blackjack.application.handler.query.GetRankingQueryHandler;
import cat.opteams.blackjack.application.mapper.RankingEntryResponse;
import cat.opteams.blackjack.application.query.GetRankingQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/ranking")
@RequiredArgsConstructor
@Validated
public class RankingController {

    private final GetRankingQueryHandler getRankingQueryHandler;

    @GetMapping
    @Operation(summary = "Get player ranking",
            description = "Retrieves the ranking of players based on total points")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking retrieved successfully")
    })
    public Flux<RankingEntryResponse> getRanking(
            @Parameter(description = "Maximum number of players to return (default: 10, min: 1, max: 100)")
            @RequestParam(required = false, defaultValue = "10")
            @Min(value = 1, message = "Limit must be at least 1")
            @Max(value = 100, message = "Limit cannot exceed 100")
            Integer limit
    ) {
        log.info("GET /ranking - Retrieving ranking with limit: {}", limit);

        GetRankingQuery query = new GetRankingQuery(limit);

        return getRankingQueryHandler.handle(query)
                .doOnComplete(() -> log.info("Ranking retrieved successfully"))
                .doOnError(error -> log.error("Error retrieving ranking: {}", error.getMessage()));
    }
}
