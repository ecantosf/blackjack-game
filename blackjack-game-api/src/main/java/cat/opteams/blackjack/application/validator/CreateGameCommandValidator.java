package cat.opteams.blackjack.application.validator;

import cat.opteams.blackjack.application.command.CreateGameCommand;
import org.springframework.stereotype.Component;

@Component
public class CreateGameCommandValidator {

    public void validate(CreateGameCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }

        if (command.playerName() == null || command.playerName().isBlank()) {
            throw new IllegalArgumentException("Player name is required");
        }

        if (command.bet() == null) {
            throw new IllegalArgumentException("Bet is required");
        }
    }
}
