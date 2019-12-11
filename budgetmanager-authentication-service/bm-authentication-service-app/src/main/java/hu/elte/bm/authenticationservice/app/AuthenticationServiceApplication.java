package hu.elte.bm.authenticationservice.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "hu.elte.bm.authenticationservice")
@PropertySource({"classpath:messages.properties", "classpath:security.properties"})
@EnableZuulProxy
public class AuthenticationServiceApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceApplication.class);

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Value("${application.database.type}")
    private String db;

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
        return args -> {
            LOGGER.info("# Application is running now. (Profile: " + activeProfile + ")");
            LOGGER.info("# With running database: " + db);
        };
    }

}
