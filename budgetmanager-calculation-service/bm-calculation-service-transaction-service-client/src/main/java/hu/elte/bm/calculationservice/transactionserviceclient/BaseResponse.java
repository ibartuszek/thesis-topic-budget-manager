package hu.elte.bm.calculationservice.transactionserviceclient;

public class BaseResponse {

    private String message;
    private boolean successful;

    public String getMessage() {
        return message;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
