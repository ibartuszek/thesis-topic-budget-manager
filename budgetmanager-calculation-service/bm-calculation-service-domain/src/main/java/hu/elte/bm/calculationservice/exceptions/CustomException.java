package hu.elte.bm.calculationservice.exceptions;

public interface CustomException {

    String getMessage();

    StackTraceElement[] getStackTrace();

    void printStackTrace();
}
