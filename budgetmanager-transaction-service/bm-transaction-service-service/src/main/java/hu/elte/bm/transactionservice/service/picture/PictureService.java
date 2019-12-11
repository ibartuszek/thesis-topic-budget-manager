package hu.elte.bm.transactionservice.service.picture;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.exceptions.PictureNotFoundException;
import hu.elte.bm.transactionservice.domain.transaction.Picture;
import hu.elte.bm.transactionservice.service.database.PictureDao;

@Service
public class PictureService {

    private final PictureDao pictureDao;

    @Value("${picture.picture_cannot_be_found:Original picture cannot be found in the repository!}")
    private String pictureNotFound;

    @Value("${picture.picture_not_null:Picture cannot be null!}")
    private String pictureNotNull;

    @Value("${picture.picture_id_not_null:Picture id cannot be null!}")
    private String pictureIdNotNull;

    @Value("${picture.picture_content_not_null:Picture content cannot be null!}")
    private String pictureContentNotNull;

    public PictureService(final PictureDao pictureDao) {
        this.pictureDao = pictureDao;
    }

    public Picture findById(final Long id, final Long userId) {
        Optional<Picture> picture = pictureDao.findById(id, userId);
        if (picture.isEmpty()) {
            throw new PictureNotFoundException(id, pictureNotFound);
        }
        return picture.get();
    }

    public Picture save(final Picture picture, final Long userId) {
        Assert.notNull(picture, pictureNotNull);
        Assert.notNull(picture.getPicture(), pictureContentNotNull);
        return pictureDao.save(picture, userId);
    }

    public Picture delete(final Picture picture, final Long userId) {
        Assert.notNull(picture, pictureNotNull);
        Assert.notNull(picture.getId(), pictureIdNotNull);
        return pictureDao.delete(picture, userId);
    }
}
