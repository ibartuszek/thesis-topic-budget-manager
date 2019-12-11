package hu.elte.bm.transactionservice.dal.picture;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface PictureRepository extends CrudRepository<PictureEntity, Long> {

    Optional<PictureEntity> findByIdAndUserId(Long id, Long userId);
}
