package hu.elte.bm.authenticationservice.web.user;

import hu.elte.bm.authenticationservice.domain.User;

public final class UserResponse extends ResponseModel {

    private User user;
    private String email;
    private Long id;

    private UserResponse() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    private static UserResponse createSuccessfulUserResponse(final String message) {
        UserResponse result = new UserResponse();
        result.setMessage(message);
        result.setSuccessful(true);
        return result;
    }

    public static UserResponse createSuccessfulUserResponse(final User user, final String message) {
        UserResponse result = createSuccessfulUserResponse(message);
        result.setUser(user);
        return result;
    }

    public static UserResponse createSuccessfulUserResponse(final String email, final String message) {
        UserResponse result = createSuccessfulUserResponse(message);
        result.setEmail(email);
        return result;
    }

    public static UserResponse createSuccessfulUserResponse(final Long id, final String message) {
        UserResponse result = createSuccessfulUserResponse(message);
        result.setId(id);
        return result;
    }

    private static UserResponse createFailureUserResponse(final String message) {
        UserResponse result = new UserResponse();
        result.setMessage(message);
        result.setSuccessful(false);
        return result;
    }

    public static UserResponse createFailureUserResponse(final User user, final String message) {
        UserResponse result = createFailureUserResponse(message);
        result.setUser(user);
        return result;
    }

    public static UserResponse createFailureUserResponse(final String email, final String message) {
        UserResponse result = createFailureUserResponse(message);
        result.setEmail(email);
        return result;
    }

    public static UserResponse createFailureUserResponse(final Long id, final String message) {
        UserResponse result = createFailureUserResponse(message);
        result.setId(id);
        return result;
    }

}
