package cat.opteams.blackjack.application.validator;

import cat.opteams.blackjack.application.command.PlayCommand;
import org.springframework.stereotype.Component;

@Component
public class PlayCommandValidator {

    public void validate(PlayCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }

        if (command.gameId() == null || command.gameId().isBlank()) {
            throw new IllegalArgumentException("Game ID is required");
        }

        if (command.action() == null) {
            throw new IllegalArgumentException("Action is required");
        }
    }
}
