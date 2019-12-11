package hu.elte.bm.calculationservice.transactionserviceclient.categories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.TransactionType;

@Component
public class MainCategoryProxy extends AbstractCategoryProxy {

    private final RestTemplate restTemplate;

    @Value("${bm.transactionservice.maincategories.findall}")
    private String findMainCategoriesUrl;

    @Autowired
    public MainCategoryProxy(final RestTemplate restTemplate) {
        super();
        this.restTemplate = restTemplate;
    }

    @Override
    public List<MainCategory> getCategories(final TransactionType type, final Long userId) {
        String url = createUrlWithTransactionTypeAndUserId(findMainCategoriesUrl, type, userId);
        ResponseEntity<MainCategoryListResponse> responseEntity = getResponseEntity(url);
        checkResponseStatus(responseEntity, findMainCategoriesUrl);
        return responseEntity.getBody().getMainCategoryList();
    }

    protected ResponseEntity<MainCategoryListResponse> getResponseEntity(final String url) {
        return restTemplate.getForEntity(url, MainCategoryListResponse.class);
    }

}
