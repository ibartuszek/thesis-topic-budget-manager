package hu.elte.bm.authenticationservice.domain;

public class UserException extends RuntimeException {

    private final User user;

    public UserException(final String message, final User user) {
        super(message);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
