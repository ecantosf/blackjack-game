package cat.opteams.blackjack.infrastructure.adapter.outgoing.mysql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("players")
public class PlayerEntity {

    @Id
    private String id;

    private String name;

    @Builder.Default
    private int totalGames = 0;

    @Builder.Default
    private int gamesWon = 0;

    @Builder.Default
    private int totalPoints = 0;
}
