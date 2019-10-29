package hu.elte.bm.calculationservice.transactionserviceclient.categories;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import hu.elte.bm.calculationservice.transactionserviceclient.AbstractProvider;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.TransactionType;

@Component
public class MainCategoryProvider extends AbstractProvider {

    @Value("${bm.transactionservice.maincategories.findall}")
    private String findMainCategoriesUrl;

    @Autowired
    public MainCategoryProvider(final RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public List<MainCategory> provide(final TransactionType type, final Long userId) {
        String url = createUrlWithTransactionTypeAndUserId(findMainCategoriesUrl, type, userId);
        ResponseEntity<MainCategory[]> responseEntity = getRestTemplate().getForEntity(url, MainCategory[].class);
        checkResponseStatus(responseEntity, findMainCategoriesUrl);
        return List.of(Objects.requireNonNull(responseEntity.getBody()));
    }

}
