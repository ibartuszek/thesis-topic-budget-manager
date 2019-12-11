package hu.elte.bm.transactionservice.dal.picture;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.transaction.Picture;

@Component
public class PictureTransformer {

    PictureEntity transformToPictureEntity(final Picture picture, final Long userId) {
        return PictureEntity.builder()
            .withId(picture.getId())
            //.withPicture(Base64.decodeBase64(picture.getPicture()))
            .withPicture(picture.getPicture())
            .withUserId(userId)
            .build();
    }

    Picture transformToPicture(final PictureEntity pictureEntity) {
        return Picture.builder()
            .withId(pictureEntity.getId())
            // .withPicture(Base64.encodeBase64String(pictureEntity.getPicture()))
            .withPicture(pictureEntity.getPicture())
            .build();
    }

}
