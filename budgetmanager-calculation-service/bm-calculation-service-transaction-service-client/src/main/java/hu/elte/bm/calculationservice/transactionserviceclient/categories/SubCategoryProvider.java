package hu.elte.bm.calculationservice.transactionserviceclient.categories;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import hu.elte.bm.calculationservice.transactionserviceclient.AbstractProvider;
import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.TransactionType;

@Component
public class SubCategoryProvider extends AbstractProvider {

    @Value("${bm.transactionservice.subcategories.findall}")
    private String findMainCategoriesUrl;

    @Autowired
    public SubCategoryProvider(final RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public List<SubCategory> provide(final TransactionType type, final Long userId) {
        String url = createUrlWithTransactionTypeAndUserId(findMainCategoriesUrl, type, userId);
        ResponseEntity<SubCategory[]> responseEntity = getRestTemplate().getForEntity(url, SubCategory[].class);
        checkResponseStatus(responseEntity, findMainCategoriesUrl);
        return List.of(Objects.requireNonNull(responseEntity.getBody()));
    }

}
