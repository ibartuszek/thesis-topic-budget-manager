package hu.elte.bm.transactionservice.service.database;

public class DatabaseException extends RuntimeException {

    public DatabaseException(final String message) {
        super(message);
    }

    public DatabaseException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

}
