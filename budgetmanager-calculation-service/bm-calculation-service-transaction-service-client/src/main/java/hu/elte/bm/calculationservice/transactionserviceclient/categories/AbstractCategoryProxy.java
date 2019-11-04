package hu.elte.bm.calculationservice.transactionserviceclient.categories;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import hu.elte.bm.calculationservice.transactionserviceclient.exceptions.TransactionServiceException;
import hu.elte.bm.transactionservice.TransactionType;

public abstract class AbstractCategoryProxy {

    protected static final String EXCEPTION_MESSAGE = "Transaction service sent: {0} during: '{1}' call.";

    protected String createUrlWithTransactionTypeAndUserId(final String baseUrl, final TransactionType type, final Long userId) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
            .queryParam("type", type)
            .queryParam("userId", userId)
            .toUriString();
    }

    public abstract List getCategories(TransactionType type, Long userId);

    protected void checkResponseStatus(final ResponseEntity responseEntity, final String url) {
        if (!responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            throw new TransactionServiceException(MessageFormat.format(EXCEPTION_MESSAGE, responseEntity.getStatusCode(), url));
        }
    }

}
