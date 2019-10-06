package hu.elte.bm.transactionservice.domain.exceptions;

import hu.elte.bm.transactionservice.domain.transaction.Picture;

public class PictureNotFoundException extends RuntimeException implements CustomException {

    private final Picture picture;

    public PictureNotFoundException(final Picture picture, final String message) {
        super(message);
        this.picture = picture;
    }

    public Picture getPicture() {
        return picture;
    }
}
