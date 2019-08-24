package hu.elte.bm.transactionservice.dal.categories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

public interface SubCategoryRepository extends CrudRepository<SubCategoryEntity, Long> {

    @Query("select c from SubCategoryEntity c "
        + "where c.transactionType = ?1 and c.userId = ?2")
    Iterable<SubCategoryEntity> findAllSubcategory(TransactionType transactionType, Long userId);

    Optional<SubCategoryEntity> findByIdAndUserId(Long id, Long userId);

    @Query("select c from SubCategoryEntity c "
        + "where c.name = ?1 and c.transactionType = ?2 and c.userId = ?3")
    Optional<SubCategoryEntity> findByName(String name, TransactionType transactionType, Long userId);
}
