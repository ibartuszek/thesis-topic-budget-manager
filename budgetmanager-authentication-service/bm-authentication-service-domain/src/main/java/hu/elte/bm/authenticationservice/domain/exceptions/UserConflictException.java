package hu.elte.bm.authenticationservice.domain.exceptions;

import hu.elte.bm.authenticationservice.domain.User;

public class UserConflictException extends RuntimeException implements UserException {

    private final User user;

    public UserConflictException(final User user, final String message) {
        super(message);
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }
}
