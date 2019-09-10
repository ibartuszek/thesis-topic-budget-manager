package hu.elte.bm.authenticationservice.domain;

public class UserCannotBeFoundException extends RuntimeException {

    public UserCannotBeFoundException(final String message) {
        super(message);
    }
}
