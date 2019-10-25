package hu.elte.bm.calculationservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("hu.elte.bm.calculationservice.dal")
@EntityScan("hu.elte.bm.calculationservice.dal")
@PropertySource("classpath:database-${application.database.type}.properties")
public class DatabaseConfiguration {
}
