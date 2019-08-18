package hu.elte.bm.authenticationservice.web.common;

public class ResponseModel {

    private String message;
    private boolean successful;

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
}
