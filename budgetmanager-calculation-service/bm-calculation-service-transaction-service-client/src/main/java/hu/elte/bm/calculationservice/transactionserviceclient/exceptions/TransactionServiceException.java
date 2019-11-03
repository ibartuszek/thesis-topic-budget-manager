package hu.elte.bm.calculationservice.transactionserviceclient.exceptions;

public class TransactionServiceException extends RuntimeException {

    public TransactionServiceException(final String message) {
        super(message);
    }

}
