package hu.elte.bm.transactionservice.web.picture;

import hu.elte.bm.transactionservice.domain.transaction.Picture;
import hu.elte.bm.transactionservice.web.common.ResponseModel;

public final class PictureResponse extends ResponseModel {

    private Picture picture;

    private PictureResponse(final String message, final boolean successful) {
        super(message, successful);
    }

    static PictureResponse createSuccessfulPictureResponse(final Picture picture, final String message) {
        PictureResponse pictureResponse = new PictureResponse(message, true);
        pictureResponse.setPicture(picture);
        return pictureResponse;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(final Picture picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "PictureResponse{"
                + "message='" + getMessage() + '\''
                + ", successful=" + isSuccessful()
                + ", picture=" + picture
                + '}';
    }
}
