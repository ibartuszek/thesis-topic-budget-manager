package hu.elte.bm.transactionservice.config;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hu.elte.bm.commonpack.validator.ModelValidator;

@Configuration
public class PublicBeanConfiguration {

    @Bean
    public DozerBeanMapper dozerBeanMapper() {
        return new DozerBeanMapper();
    }

    @Bean
    public ModelValidator modelValidator() {
        return new ModelValidator();
    }

}
