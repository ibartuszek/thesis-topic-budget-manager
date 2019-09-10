package hu.elte.bm.authenticationservice.domain;

public class UserEmailIsReservedException extends RuntimeException {

    private final String email;

    public UserEmailIsReservedException(final String message, final String email) {
        super(message);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
