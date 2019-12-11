package hu.elte.bm.transactionservice.domain.exceptions.transaction;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;

public class TransactionNotFoundException extends RuntimeException implements TransactionException {

    private final Transaction transaction;

    public TransactionNotFoundException(final Transaction transaction, final String message) {
        super(message);
        this.transaction = transaction;
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}
