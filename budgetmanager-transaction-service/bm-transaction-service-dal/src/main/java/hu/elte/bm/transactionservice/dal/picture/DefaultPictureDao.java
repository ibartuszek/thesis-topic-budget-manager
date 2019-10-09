package hu.elte.bm.transactionservice.dal.picture;

import java.util.Optional;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.transaction.Picture;
import hu.elte.bm.transactionservice.service.database.PictureDao;

@Component
public class DefaultPictureDao implements PictureDao {

    private final PictureRepository pictureRepository;
    private final PictureTransformer pictureTransformer;

    DefaultPictureDao(final PictureRepository pictureRepository, final PictureTransformer pictureTransformer) {
        this.pictureRepository = pictureRepository;
        this.pictureTransformer = pictureTransformer;
    }

    @Override
    public Optional<Picture> findById(final Long id, final Long userId) {
        Optional<PictureEntity> pictureEntity = pictureRepository.findByIdAndUserId(id, userId);
        return pictureEntity.map(pictureTransformer::transformToPicture);
    }

    @Override
    public Picture save(final Picture picture, final Long userId) {
        PictureEntity pictureEntity = pictureRepository.save(pictureTransformer.transformToPictureEntity(picture, userId));
        return pictureTransformer.transformToPicture(pictureEntity);
    }

    @Override
    public Picture delete(final Picture picture, final Long userId) {
        pictureRepository.delete(pictureTransformer.transformToPictureEntity(picture, userId));
        return picture;
    }
}
