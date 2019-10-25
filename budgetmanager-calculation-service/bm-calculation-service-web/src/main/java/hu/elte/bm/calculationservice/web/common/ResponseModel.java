package hu.elte.bm.calculationservice.web.common;

public class ResponseModel {

    private String message;
    private boolean successful;

    public ResponseModel(final String message, final boolean successful) {
        this.message = message;
        this.successful = successful;
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
