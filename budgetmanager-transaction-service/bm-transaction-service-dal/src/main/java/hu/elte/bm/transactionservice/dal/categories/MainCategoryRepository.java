package hu.elte.bm.transactionservice.dal.categories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import hu.elte.bm.transactionservice.domain.categories.CategoryType;

public interface MainCategoryRepository extends CrudRepository<MainCategoryEntity, Long> {

    @Query("select c from MainCategoryEntity c "
        + "where c.categoryType = ?1")
    Iterable<MainCategoryEntity> findAllMaincategory(CategoryType categoryType);

    @Query("select c from MainCategoryEntity c "
        + "where c.name = ?1 and c.categoryType = ?2")
    Optional<MainCategoryEntity> findByName(String name, CategoryType type);
}
