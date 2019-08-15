package hu.elte.bm.authenticationservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("hu.elte.bm.authenticationservice.dal")
@EntityScan("hu.elte.bm.authenticationservice.dal")
@PropertySource("classpath:database-mysql.properties")
public class DatabaseConfiguration {
}
