package hu.elte.bm.transactionservice.web.common;

public class ResponseModel {

    private String message;
    private boolean successful;

    protected ResponseModel(final String message, final boolean successful) {
        this.message = message;
        this.successful = successful;
    }

    public static ResponseModel createSuccessfulResponse(final String message) {
        return new ResponseModel(message, true);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(final boolean successful) {
        this.successful = successful;
    }

    @Override
    public String toString() {
        return "ResponseModel{"
            + "message='" + message + '\''
            + ", successful=" + successful
            + '}';
    }
}
