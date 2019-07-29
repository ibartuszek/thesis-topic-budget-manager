package hu.elte.bm.transactionservice.domain.transaction;

public class TransactionException extends RuntimeException {

    private final Transaction transaction;

    public TransactionException(final Transaction transaction, final String message) {
        super(message);
        this.transaction = transaction;
    }

    public TransactionException(final Transaction transaction, final String message,
        final Throwable throwable) {
        super(message, throwable);
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
