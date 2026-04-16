package cat.opteams.blackjack.infrastructure.adapter.incoming.web;

import cat.opteams.blackjack.application.handler.command.UpdatePlayerNameHandler;
import cat.opteams.blackjack.application.mapper.RankingEntryResponse;
import cat.opteams.blackjack.infrastructure.adapter.incoming.web.dto.UpdatePlayerNameRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@DisplayName("PlayerController Tests")
class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UpdatePlayerNameHandler updatePlayerNameHandler;

    @Test
    @DisplayName("Should update player name and return 200")
    void shouldUpdatePlayerNameAndReturn200() {
        String playerId = UUID.randomUUID().toString();
        UpdatePlayerNameRequest request = new UpdatePlayerNameRequest("NewName");
        RankingEntryResponse response = new RankingEntryResponse(
                playerId, "NewName", 5, 3, 300, 0.6
        );

        when(updatePlayerNameHandler.handle(any())).thenReturn(Mono.just(response));

        webTestClient.patch()
                .uri("/players/{playerId}/name", playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.playerName").isEqualTo("NewName");
    }

    @Test
    @DisplayName("Should return 400 when update name request is invalid (empty name)")
    void shouldReturn400WhenUpdateNameRequestIsEmpty() {
        String playerId = UUID.randomUUID().toString();
        UpdatePlayerNameRequest invalidRequest = new UpdatePlayerNameRequest("");

        webTestClient.patch()
                .uri("/players/{playerId}/name", playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Should return 400 when update name request is invalid (null name)")
    void shouldReturn400WhenUpdateNameRequestIsNull() {
        String playerId = UUID.randomUUID().toString();
        String invalidRequest = "{\"newName\": null}";

        webTestClient.patch()
                .uri("/players/{playerId}/name", playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Should return 400 when name is too long")
    void shouldReturn400WhenNameIsTooLong() {
        String playerId = UUID.randomUUID().toString();
        String longName = "A".repeat(51);
        UpdatePlayerNameRequest invalidRequest = new UpdatePlayerNameRequest(longName);

        webTestClient.patch()
                .uri("/players/{playerId}/name", playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
