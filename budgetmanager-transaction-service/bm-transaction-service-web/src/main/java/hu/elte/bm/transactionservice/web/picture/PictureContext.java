package hu.elte.bm.transactionservice.web.picture;

import javax.validation.constraints.NotNull;

import hu.elte.bm.transactionservice.domain.transaction.Picture;

public class PictureContext {

    @NotNull(message = "User id cannot be null!")
    private Long userId;

    @NotNull(message = "Picture cannot be null!")
    private Picture picture;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(final Picture picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "PictureContext{"
            + "userId=" + userId
            + ", picture=" + picture
            + '}';
    }
}
