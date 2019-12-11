package hu.elte.bm.authenticationservice.domain.exceptions;

import hu.elte.bm.authenticationservice.domain.User;

public class IllegalUserException extends RuntimeException implements UserException {

    private final User user;

    public IllegalUserException(final User user, final String message) {
        super(message);
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }
}
