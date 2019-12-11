package hu.elte.bm.calculationservice.transactionserviceclient;

import java.text.MessageFormat;

import org.springframework.http.ResponseEntity;

import hu.elte.bm.calculationservice.transactionserviceclient.exceptions.TransactionServiceException;

public class BaseProxy {

    protected static final String EXCEPTION_MESSAGE = "Transaction service sent: {0} during: '{1}' call.";

    protected void checkResponseStatus(final ResponseEntity responseEntity, final String url) {
        if (!responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            throw new TransactionServiceException(MessageFormat.format(EXCEPTION_MESSAGE, responseEntity.getStatusCode(), url));
        }
    }

}
