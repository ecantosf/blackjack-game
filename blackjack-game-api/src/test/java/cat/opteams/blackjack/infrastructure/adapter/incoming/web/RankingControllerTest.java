package cat.opteams.blackjack.infrastructure.adapter.incoming.web;

import cat.opteams.blackjack.application.handler.query.GetRankingQueryHandler;
import cat.opteams.blackjack.application.mapper.RankingEntryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@DisplayName("RankingController Tests")
class RankingControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GetRankingQueryHandler getRankingQueryHandler;

    @Test
    @DisplayName("Should get ranking and return 200")
    void shouldGetRankingAndReturn200() {
        List<RankingEntryResponse> ranking = List.of(
                new RankingEntryResponse(UUID.randomUUID().toString(), "Player1", 10, 7, 700, 0.7),
                new RankingEntryResponse(UUID.randomUUID().toString(), "Player2", 10, 5, 500, 0.5)
        );

        when(getRankingQueryHandler.handle(any())).thenReturn(Flux.fromIterable(ranking));

        webTestClient.get()
                .uri("/ranking?limit=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].playerName").isEqualTo("Player1")
                .jsonPath("$[1].playerName").isEqualTo("Player2");
    }

    @Test
    @DisplayName("Should use default limit when not provided")
    void shouldUseDefaultLimitWhenNotProvided() {
        when(getRankingQueryHandler.handle(any())).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/ranking")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Should handle empty ranking")
    void shouldHandleEmptyRanking() {
        when(getRankingQueryHandler.handle(any())).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/ranking?limit=5")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(0);
    }
}
