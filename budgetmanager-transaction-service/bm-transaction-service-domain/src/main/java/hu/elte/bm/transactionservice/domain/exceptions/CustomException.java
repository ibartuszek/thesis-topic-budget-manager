package hu.elte.bm.transactionservice.domain.exceptions;

public interface CustomException {

    String getMessage();

    StackTraceElement[] getStackTrace();

    void printStackTrace();
}
