package cat.opteams.blackjack.infrastructure.adapter.outgoing.mongodb;

import cat.opteams.blackjack.domain.model.aggregate.Game;
import cat.opteams.blackjack.domain.model.valueobject.*;
import cat.opteams.blackjack.domain.model.valueobject.Card;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GameDocumentMapper {

    /**
     * Converts a Game (domain) to GameDocument (persistence).
     * @param game the domain aggregate
     * @return document for MongoDB, or null if game is null
     */
    public GameDocument toDocument(Game game) {
        if (game == null) {
            log.warn("Attempted to convert null Game to document");
            return null;
        }

        try {
            return GameDocument.builder()
                    .id(game.getId().getValue().toString())
                    .playerId(game.getPlayerId().getValue().toString())
                    .playerHand(toCardDocumentList(game.getPlayerHand()))
                    .dealerHand(toCardDocumentList(game.getDealerHand()))
                    .bet(game.getBet().getAmount())
                    .status(game.getStatus().name())
                    .winner(game.getWinner() != null ? game.getWinner().name() : null)
                    .createdAt(game.getCreatedAt())
                    .finishedAt(game.getFinishedAt())
                    .build();
        } catch (Exception e) {
            log.error("Error converting Game to GameDocument: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to convert Game to document", e);
        }
    }

    /**
     * Converts a GameDocument (persistence) to Game (domain).
     * @param document MongoDB document
     * @return domain aggregate, or null if document is null
     */
    public Game toDomain(GameDocument document) {
        if (document == null) {
            log.warn("Attempted to convert null GameDocument to domain");
            return null;
        }

        try {
            if (document.getId() == null || document.getId().isBlank()) {
                throw new IllegalArgumentException("GameDocument has null or blank id");
            }
            if (document.getPlayerId() == null || document.getPlayerId().isBlank()) {
                throw new IllegalArgumentException("GameDocument has null or blank playerId");
            }
            if (document.getBet() == null) {
                throw new IllegalArgumentException("GameDocument has null bet");
            }
            if (document.getStatus() == null) {
                throw new IllegalArgumentException("GameDocument has null status");
            }

            return reconstructGame(document);
        } catch (Exception e) {
            log.error("Error converting GameDocument to Game: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to convert document to Game", e);
        }
    }

    private Game reconstructGame(GameDocument document) {
        GameId gameId = new GameId(document.getId());
        PlayerId playerId = new PlayerId(document.getPlayerId());
        Money bet = new Money(document.getBet());

        Hand playerHand = toHand(document.getPlayerHand());
        Hand dealerHand = toHand(document.getDealerHand());

        GameStatus status = GameStatus.valueOf(document.getStatus());
        Game.Player winner = document.getWinner() != null ?
                Game.Player.valueOf(document.getWinner()) : null;

        return new Game(
                gameId, playerId, bet, playerHand, dealerHand,
                status, winner, document.getCreatedAt(), document.getFinishedAt()
        );
    }

    private List<CardDocument> toCardDocumentList(Hand hand) {
        if (hand == null) {
            return List.of();
        }

        return hand.getCards().stream()
                .map(this::toCardDocument)
                .collect(Collectors.toList());
    }

    private CardDocument toCardDocument(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }

        return CardDocument.builder()
                .suit(card.suit().name())
                .rank(card.rank().name())
                .value(card.getValue())
                .build();
    }

    private Hand toHand(List<CardDocument> cardDocuments) {
        Hand hand = new Hand();

        if (cardDocuments == null) {
            return hand;
        }

        for (CardDocument doc : cardDocuments) {
            if (doc != null) {
                Card card = toCard(doc);
                hand = hand.addCard(card);
            }
        }
        return hand;
    }

    private Card toCard(CardDocument document) {
        if (document == null) {
            throw new IllegalArgumentException("CardDocument cannot be null");
        }
        if (document.getSuit() == null || document.getRank() == null) {
            throw new IllegalArgumentException("CardDocument missing suit or rank");
        }

        Suit suit = Suit.valueOf(document.getSuit());
        Rank rank = Rank.valueOf(document.getRank());
        return new Card(suit, rank);
    }
}
