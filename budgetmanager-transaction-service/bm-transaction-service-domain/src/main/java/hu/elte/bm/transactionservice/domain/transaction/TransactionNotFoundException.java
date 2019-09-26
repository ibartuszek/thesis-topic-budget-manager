package hu.elte.bm.transactionservice.domain.transaction;

public class TransactionNotFoundException extends RuntimeException {

    private final Transaction transaction;

    public TransactionNotFoundException(final Transaction transaction, final String message) {
        super(message);
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
