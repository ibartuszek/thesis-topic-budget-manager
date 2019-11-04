package hu.elte.bm.calculationservice.app.test.mocks;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import hu.elte.bm.calculationservice.transactionserviceclient.categories.MainCategoryProxy;
import hu.elte.bm.calculationservice.transactionserviceclient.categories.SubCategoryProxy;

@TestConfiguration
public class MockConfiguration {

    @Bean
    MainCategoryProxy mainCategoryProxy() {
        return new MainCategoryMockProxy();
    }

    @Bean
    SubCategoryProxy subCategoryProxy() {
        return new SubCategoryMockProxy();
    }

}
