package hu.elte.bm.calculationservice.transactionserviceclient.transactions;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import hu.elte.bm.calculationservice.transactionserviceclient.BaseProxy;
import hu.elte.bm.transactionservice.Transaction;
import hu.elte.bm.transactionservice.TransactionType;

@Component
public class TransactionProxy extends BaseProxy {

    private final RestTemplate restTemplate;

    @Value("${bm.transactionservice.transactions.findall}")
    private String findTransactionsUrl;

    @Autowired
    public TransactionProxy(final RestTemplate restTemplate) {
        super();
        this.restTemplate = restTemplate;
    }

    public List<Transaction> getTransactions(final TransactionType type, final Long userId,
        final LocalDate start, final LocalDate end) {
        String url = createUrl(type, userId, start, end);
        ResponseEntity<TransactionListResponse> responseEntity = getResponseEntity(url);
        checkResponseStatus(responseEntity, findTransactionsUrl);
        return responseEntity.getBody().getTransactionList();
    }

    private String createUrl(final TransactionType type, final Long userId, final LocalDate start, final LocalDate end) {
        return UriComponentsBuilder.fromHttpUrl(findTransactionsUrl)
            .queryParam("type", type)
            .queryParam("userId", userId)
            .queryParam("start", start)
            .queryParam("end", end)
            .toUriString();
    }

    protected ResponseEntity<TransactionListResponse> getResponseEntity(final String url) {
        return restTemplate.getForEntity(url, TransactionListResponse.class);
    }

}
