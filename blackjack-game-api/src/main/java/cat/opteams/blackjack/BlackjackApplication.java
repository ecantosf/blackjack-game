package cat.opteams.blackjack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableCaching
@EnableReactiveMongoRepositories(basePackages = "cat.opteams.blackjack.infrastructure.adapter.outgoing.mongodb")
@EnableR2dbcRepositories(basePackages = "cat.opteams.blackjack.infrastructure.adapter.outgoing.mysql")
public class BlackjackApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlackjackApplication.class, args);
    }
}
