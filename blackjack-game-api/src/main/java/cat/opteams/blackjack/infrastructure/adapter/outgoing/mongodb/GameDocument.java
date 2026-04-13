package cat.opteams.blackjack.infrastructure.adapter.outgoing.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "games")
public class GameDocument {

    @Id
    private String id;

    private String playerId;

    private List<CardDocument> playerHand;

    private List<CardDocument> dealerHand;

    private BigDecimal bet;

    private String status;

    private String winner;

    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;
}
