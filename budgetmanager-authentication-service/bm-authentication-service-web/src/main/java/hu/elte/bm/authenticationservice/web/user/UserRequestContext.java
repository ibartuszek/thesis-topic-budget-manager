package hu.elte.bm.authenticationservice.web.user;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.authenticationservice.web.validation.ValidUserId;

public class UserRequestContext {

    @NotNull
    @ValidUserId
    private Long userId;

    @Valid
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }
}
