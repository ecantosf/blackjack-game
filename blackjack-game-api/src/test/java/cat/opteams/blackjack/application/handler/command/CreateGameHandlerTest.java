package cat.opteams.blackjack.application.handler.command;

import cat.opteams.blackjack.UnitTest;
import cat.opteams.blackjack.application.command.CreateGameCommand;
import cat.opteams.blackjack.application.mapper.GameResponse;
import cat.opteams.blackjack.application.mapper.GameResponseMapper;
import cat.opteams.blackjack.application.validator.CreateGameCommandValidator;
import cat.opteams.blackjack.domain.model.aggregate.Game;
import cat.opteams.blackjack.domain.model.entity.Player;
import cat.opteams.blackjack.domain.model.valueobject.Money;
import cat.opteams.blackjack.domain.port.outgoing.DeckProviderPort;
import cat.opteams.blackjack.domain.port.outgoing.GameRepositoryPort;
import cat.opteams.blackjack.domain.port.outgoing.PlayerRepositoryPort;
import cat.opteams.blackjack.domain.service.Deck;
import cat.opteams.blackjack.testutil.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateGameHandler Tests")
class CreateGameHandlerTest extends UnitTest {

    @Mock
    private CreateGameCommandValidator validator;

    @Mock
    private PlayerRepositoryPort playerRepository;

    @Mock
    private GameRepositoryPort gameRepository;

    @Mock
    private DeckProviderPort deckProvider;

    @Mock
    private GameResponseMapper responseMapper;

    @InjectMocks
    private CreateGameHandler handler;

    private CreateGameCommand command;
    private Player player;
    private GameResponse mockResponse;

    @BeforeEach
    void setUp() {
        command = new CreateGameCommand("TestPlayer", new BigDecimal("100"));
        player = TestDataFactory.createDefaultPlayer();

        mockResponse = new GameResponse(
                "game-id-123",
                "player-id-123",
                "TestPlayer",
                List.of(),
                0,
                List.of(),
                0,
                new BigDecimal("100"),
                "IN_PROGRESS",
                null,
                LocalDateTime.now(),
                null
        );
    }

    @Test
    @DisplayName("Should create game successfully when player exists")
    void shouldCreateGameSuccessfullyWhenPlayerExists() {
        Deck deck = new Deck(42L);
        Money bet = new Money(new BigDecimal("100"));
        Game game = Game.startNewGame(player.getId(), bet);

        when(playerRepository.findByName(any())).thenReturn(Mono.just(player));
        when(deckProvider.getNewDeck()).thenReturn(Mono.just(deck));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(game));
        when(responseMapper.toResponse(any(Game.class), eq("TestPlayer"))).thenReturn(mockResponse);
        doNothing().when(validator).validate(any());

        StepVerifier.create(handler.handle(command))
                // Then
                .expectNext(mockResponse)
                .verifyComplete();

        verify(playerRepository, times(1)).findByName(any());
        verify(gameRepository, times(1)).save(any(Game.class));
        verify(responseMapper, times(1)).toResponse(any(Game.class), eq("TestPlayer"));
    }

    @Test
    @DisplayName("Should create game successfully when player does not exist")
    void shouldCreateGameSuccessfullyWhenPlayerDoesNotExist() {
        Deck deck = new Deck(42L);
        Money bet = new Money(new BigDecimal("100"));
        Game game = Game.startNewGame(player.getId(), bet);

        when(playerRepository.findByName(any())).thenReturn(Mono.empty());
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(player));
        when(deckProvider.getNewDeck()).thenReturn(Mono.just(deck));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(game));
        when(responseMapper.toResponse(any(Game.class), eq("TestPlayer"))).thenReturn(mockResponse);
        doNothing().when(validator).validate(any());

        StepVerifier.create(handler.handle(command))
                // Then
                .expectNext(mockResponse)
                .verifyComplete();

        verify(playerRepository, times(1)).save(any(Player.class));
        verify(gameRepository, times(1)).save(any(Game.class));
        verify(responseMapper, times(1)).toResponse(any(Game.class), eq("TestPlayer"));
    }
}
