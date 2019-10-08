package hu.elte.bm.authenticationservice.domain.exceptions;

import hu.elte.bm.authenticationservice.domain.User;

public class UserNotFoundException extends RuntimeException implements UserException {

    private final String userName;
    private final Long userId;
    private final User user;

    public UserNotFoundException(final String userName, final String message) {
        super(message);
        this.userName = userName;
        this.userId = null;
        this.user = null;
    }

    public UserNotFoundException(final Long userId, final String message) {
        super(message);
        this.userName = null;
        this.userId = userId;
        this.user = null;
    }

    public UserNotFoundException(final User user, final String message) {
        super(message);
        this.userName = null;
        this.userId = null;
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public User getUser() {
        return user;
    }
}
