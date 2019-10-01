package hu.elte.bm.transactionservice.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "hu.elte.bm.transactionservice")
@PropertySource({ "classpath:common_constraints.properties", "classpath:messages.properties" })
public class TransactionServiceApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceApplication.class);

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Value("${application.database.type}")
    private String db;

    public static void main(String[] args) {
        SpringApplication.run(TransactionServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

//            LOGGER.info("# Let's inspect the beans provided by Spring Boot:");
//
//            String[] beanNames = ctx.getBeanDefinitionNames();
//            Arrays.sort(beanNames);
//            for (String beanName : beanNames) {
//                LOGGER.info("# " + beanName);
//            }

            LOGGER.info("# Application is running now. (Profile: " + activeProfile + ")");
            LOGGER.info("# With running database: " + db);
        };
    }

}

