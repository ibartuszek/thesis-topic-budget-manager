package hu.elte.bm.transactionservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("hu.elte.bm.transactionservice.dal")
@EntityScan("hu.elte.bm.transactionservice.dal")
@PropertySource("classpath:database-${application.database.type}.properties")
public class DatabaseConfiguration {
}
