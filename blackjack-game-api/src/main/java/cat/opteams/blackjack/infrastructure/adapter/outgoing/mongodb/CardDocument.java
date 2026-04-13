package cat.opteams.blackjack.infrastructure.adapter.outgoing.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDocument {

    private String suit;
    private String rank;
    private Integer value;
}
