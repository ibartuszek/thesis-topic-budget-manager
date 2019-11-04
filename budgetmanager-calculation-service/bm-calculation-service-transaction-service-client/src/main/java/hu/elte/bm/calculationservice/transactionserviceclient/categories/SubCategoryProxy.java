package hu.elte.bm.calculationservice.transactionserviceclient.categories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import hu.elte.bm.transactionservice.SubCategory;
import hu.elte.bm.transactionservice.TransactionType;

@Component
public class SubCategoryProxy extends AbstractCategoryProxy {

    private final RestTemplate restTemplate;

    @Value("${bm.transactionservice.subcategories.findall}")
    private String findSubCategoriesUrl;

    @Autowired
    public SubCategoryProxy(final RestTemplate restTemplate) {
        super();
        this.restTemplate = restTemplate;
    }

    @Override
    public List<SubCategory> getCategories(final TransactionType type, final Long userId) {
        String url = createUrlWithTransactionTypeAndUserId(findSubCategoriesUrl, type, userId);
        ResponseEntity<SubCategoryListResponse> responseEntity = getResponseEntity(url);
        checkResponseStatus(responseEntity, findSubCategoriesUrl);
        return responseEntity.getBody().getSubCategoryList();
    }

    protected ResponseEntity<SubCategoryListResponse> getResponseEntity(final String url) {
        return restTemplate.getForEntity(url, SubCategoryListResponse.class);
    }

}
