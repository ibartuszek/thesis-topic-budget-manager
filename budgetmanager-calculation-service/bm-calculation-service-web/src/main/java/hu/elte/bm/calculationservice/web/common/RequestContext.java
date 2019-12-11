package hu.elte.bm.calculationservice.web.common;

import javax.validation.constraints.NotNull;

public class RequestContext {

    @NotNull(message = "User id cannot be null!")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RequestContext{"
                + ", userId=" + userId
                + '}';
    }
}
