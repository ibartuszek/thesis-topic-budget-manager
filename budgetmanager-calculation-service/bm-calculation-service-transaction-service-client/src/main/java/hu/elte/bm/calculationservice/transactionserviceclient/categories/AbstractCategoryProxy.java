package hu.elte.bm.calculationservice.transactionserviceclient.categories;

import java.util.List;

import org.springframework.web.util.UriComponentsBuilder;

import hu.elte.bm.calculationservice.transactionserviceclient.BaseProxy;
import hu.elte.bm.transactionservice.TransactionType;

public abstract class AbstractCategoryProxy extends BaseProxy {

    protected String createUrlWithTransactionTypeAndUserId(final String baseUrl, final TransactionType type, final Long userId) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
            .queryParam("type", type)
            .queryParam("userId", userId)
            .toUriString();
    }

    public abstract List getCategories(TransactionType type, Long userId);

}
