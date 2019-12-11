package hu.elte.bm.authenticationservice.web.user;

public class ResponseModel {

    private String message;
    private boolean successful;

    protected ResponseModel() {
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

    public static ResponseModel createResponseModel(final String message, final boolean successful) {
        ResponseModel result = new ResponseModel();
        result.setMessage(message);
        result.setSuccessful(successful);
        return result;
    }
}
