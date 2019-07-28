package hu.elte.bm.transactionservice.dal.categories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import hu.elte.bm.transactionservice.domain.categories.CategoryType;

public interface SubCategoryRepository extends CrudRepository<SubCategoryEntity, Long> {

    @Query("select c from SubCategoryEntity c "
        + "where c.categoryType = ?1")
    Iterable<SubCategoryEntity> findAllSubcategory(CategoryType categoryType);

    @Query("select c from SubCategoryEntity c "
        + "where c.name = ?1 and c.categoryType = ?2")
    Optional<SubCategoryEntity> findByName(String name, CategoryType type);
}
