package hu.elte.bm.authenticationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import hu.elte.bm.commonpack.validator.ModelValidator;

@Configuration
public class PublicBeans {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelValidator modelValidator() {
        return new ModelValidator();
    }
}
