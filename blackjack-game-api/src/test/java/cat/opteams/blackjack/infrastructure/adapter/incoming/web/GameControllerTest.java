package cat.opteams.blackjack.infrastructure.adapter.incoming.web;

import cat.opteams.blackjack.application.handler.command.CreateGameHandler;
import cat.opteams.blackjack.application.handler.command.DeleteGameHandler;
import cat.opteams.blackjack.application.handler.command.PlayHandler;
import cat.opteams.blackjack.application.handler.query.GetGameQueryHandler;
import cat.opteams.blackjack.application.mapper.GameResponse;
import cat.opteams.blackjack.infrastructure.adapter.incoming.web.dto.CreateGameRequest;
import cat.opteams.blackjack.infrastructure.adapter.incoming.web.dto.PlayRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@DisplayName("GameController Tests")
class GameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CreateGameHandler createGameHandler;

    @MockBean
    private PlayHandler playHandler;

    @MockBean
    private DeleteGameHandler deleteGameHandler;

    @MockBean
    private GetGameQueryHandler getGameQueryHandler;

    @Test
    @DisplayName("Should create game and return 201")
    void shouldCreateGameAndReturn201() {
        CreateGameRequest request = new CreateGameRequest("TestPlayer", new BigDecimal("100"));
        GameResponse response = createMockGameResponse();

        when(createGameHandler.handle(any())).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.playerName").isEqualTo("TestPlayer");
    }

    @Test
    @DisplayName("Should return 400 when create game request is invalid")
    void shouldReturn400WhenCreateGameRequestIsInvalid() {
        CreateGameRequest invalidRequest = new CreateGameRequest("", new BigDecimal("-100"));

        webTestClient.post()
                .uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Should get game and return 200")
    void shouldGetGameAndReturn200() {
        String gameId = UUID.randomUUID().toString();
        GameResponse response = createMockGameResponse();

        when(getGameQueryHandler.handle(any())).thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/games/{id}", gameId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty();
    }

    @Test
    @DisplayName("Should execute HIT action and return 200")
    void shouldExecuteHitActionAndReturn200() {
        String gameId = UUID.randomUUID().toString();
        PlayRequest request = new PlayRequest(PlayRequest.Action.HIT);
        GameResponse response = createMockGameResponse();

        when(playHandler.handle(any())).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/games/{id}/play", gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty();
    }

    @Test
    @DisplayName("Should execute STAND action and return 200")
    void shouldExecuteStandActionAndReturn200() {
        String gameId = UUID.randomUUID().toString();
        PlayRequest request = new PlayRequest(PlayRequest.Action.STAND);
        GameResponse response = createMockGameResponse();

        when(playHandler.handle(any())).thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/games/{id}/play", gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty();
    }

    @Test
    @DisplayName("Should return 400 when play action is invalid")
    void shouldReturn400WhenPlayActionIsInvalid() {
        String gameId = UUID.randomUUID().toString();
        String invalidRequest = "{\"action\": \"INVALID\"}";

        webTestClient.post()
                .uri("/games/{id}/play", gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Should delete game and return 204")
    void shouldDeleteGameAndReturn204() {
        String gameId = UUID.randomUUID().toString();

        when(deleteGameHandler.handle(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/games/{id}", gameId)
                .exchange()
                .expectStatus().isNoContent();
    }

    private GameResponse createMockGameResponse() {
        return new GameResponse(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "TestPlayer",
                null,
                20,
                null,
                18,
                new BigDecimal("100"),
                "IN_PROGRESS",
                null,
                LocalDateTime.now(),
                null
        );
    }
}
