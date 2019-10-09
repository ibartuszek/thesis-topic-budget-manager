package hu.elte.bm.transactionservice.domain.exceptions;

import hu.elte.bm.transactionservice.domain.transaction.Picture;

public class PictureNotFoundException extends RuntimeException implements CustomException {

    private final Picture picture;
    private final Long pictureId;

    public PictureNotFoundException(final Picture picture, final String message) {
        super(message);
        this.picture = picture;
        this.pictureId = null;
    }

    public PictureNotFoundException(final Long pictureId, final String message) {
        super(message);
        this.picture = null;
        this.pictureId = pictureId;
    }

    public Picture getPicture() {
        return picture;
    }

    public Long getPictureId() {
        return pictureId;
    }
}
