package hu.elte.bm.transactionservice.dal.categories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public interface MainCategoryRepository extends CrudRepository<MainCategoryEntity, Long> {

    @Query("select c from MainCategoryEntity c "
        + "where c.transactionType = ?1 and c.userId = ?2")
    Iterable<MainCategoryEntity> findAllMainCategory(TransactionType transactionType, Long userId);

    Optional<MainCategoryEntity> findByIdAndUserId(Long id, Long userId);

    @Query("select c from MainCategoryEntity c "
        + "where c.name = ?1 and c.transactionType = ?2 and c.userId = ?3")
    Optional<MainCategoryEntity> findByName(String name, TransactionType transactionType, Long userId);
}
