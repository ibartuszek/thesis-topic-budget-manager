package hu.elte.bm.transactionservice.domain.transaction;

public class TransactionConflictException extends RuntimeException {

    private final Transaction transaction;

    public TransactionConflictException(final Transaction transaction, final String message) {
        super(message);
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
