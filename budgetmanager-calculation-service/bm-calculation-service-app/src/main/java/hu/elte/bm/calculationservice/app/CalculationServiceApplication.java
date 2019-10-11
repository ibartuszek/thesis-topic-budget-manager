package hu.elte.bm.calculationservice.app;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CalculationServiceApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CalculationServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
        return args -> {

            LOGGER.info("# Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                LOGGER.info("# " + beanName);
            }

            LOGGER.info("# Application is running now.");
        };
    }

}
