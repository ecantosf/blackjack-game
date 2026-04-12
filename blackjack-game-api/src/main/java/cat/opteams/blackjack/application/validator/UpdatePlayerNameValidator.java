package cat.opteams.blackjack.application.validator;

import cat.opteams.blackjack.application.command.UpdatePlayerNameCommand;
import org.springframework.stereotype.Component;

@Component
public class UpdatePlayerNameValidator {

    public void validate(UpdatePlayerNameCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }

        if (command.playerId() == null || command.playerId().isBlank()) {
            throw new IllegalArgumentException("Player ID is required");
        }

        if (command.newName() == null || command.newName().isBlank()) {
            throw new IllegalArgumentException("New name is required");
        }
    }
}
