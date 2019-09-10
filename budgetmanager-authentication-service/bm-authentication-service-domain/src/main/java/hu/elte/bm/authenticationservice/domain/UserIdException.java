package hu.elte.bm.authenticationservice.domain;

public class UserIdException  extends RuntimeException {

    private final String userId;

    public UserIdException(final String message, final String userId) {
        super(message);
        this.userId = userId;
    }

    public UserIdException(final String message, final String userId, final Throwable throwable) {
        super(message, throwable);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
