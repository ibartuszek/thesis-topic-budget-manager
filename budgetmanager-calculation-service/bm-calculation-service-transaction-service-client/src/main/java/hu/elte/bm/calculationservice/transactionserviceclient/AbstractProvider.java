package hu.elte.bm.calculationservice.transactionserviceclient;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import hu.elte.bm.calculationservice.transactionserviceclient.exception.TransactionServiceException;
import hu.elte.bm.transactionservice.TransactionType;

public abstract class AbstractProvider {

    protected static final String EXCEPTION_MESSAGE = "Transaction service sent: {0} during: '{1}' call.";

    private final RestTemplate restTemplate;

    public AbstractProvider(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public abstract List provide(TransactionType type, Long userId);

    protected String createUrlWithTransactionTypeAndUserId(final String baseUrl, final TransactionType type, final Long userId) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("type", type)
                .queryParam("userId", userId)
                .toUriString();
    }

    protected void checkResponseStatus(final ResponseEntity responseEntity, final String url) {
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new TransactionServiceException(MessageFormat.format(EXCEPTION_MESSAGE, responseEntity.getStatusCode(), url));
        }
    }

}
