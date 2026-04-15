package cat.opteams.blackjack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = "cat.opteams.blackjack")
@EnableReactiveMongoRepositories(basePackages = "cat.opteams.blackjack.infrastructure.adapter.outgoing.mongodb")
@EnableR2dbcRepositories(basePackages = "cat.opteams.blackjack.infrastructure.adapter.outgoing.mysql")
public class BlackjackApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlackjackApplication.class, args);
    }
}
