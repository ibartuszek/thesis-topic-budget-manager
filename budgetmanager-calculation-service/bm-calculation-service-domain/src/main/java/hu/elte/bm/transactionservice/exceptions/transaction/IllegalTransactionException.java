package hu.elte.bm.transactionservice.exceptions.transaction;

import hu.elte.bm.transactionservice.Transaction;

public class IllegalTransactionException extends RuntimeException implements TransactionException {

    private final Transaction transaction;

    public IllegalTransactionException(final Transaction transaction, final String message) {
        super(message);
        this.transaction = transaction;
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}
