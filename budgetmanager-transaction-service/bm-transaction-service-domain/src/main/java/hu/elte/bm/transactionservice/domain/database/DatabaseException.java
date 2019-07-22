package hu.elte.bm.transactionservice.domain.database;

public class DatabaseException extends RuntimeException {

    public DatabaseException(final String message) {
        super(message);
    }

    public DatabaseException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

}
