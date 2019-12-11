package hu.elte.bm.transactionservice.service.database;

import java.util.Optional;

import hu.elte.bm.transactionservice.domain.transaction.Picture;

public interface PictureDao {

    Optional<Picture> findById(Long id, Long userId);

    Picture save(Picture picture, Long userId);

    Picture delete(Picture picture, Long userId);

}
