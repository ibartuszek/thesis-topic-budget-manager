package hu.elte.bm.transactionservice.service.database;

import java.util.List;
import java.util.Optional;

import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

public interface SubCategoryDao {

    List<SubCategory> findAll(TransactionContext context);

    Optional<SubCategory> findById(Long id, TransactionContext context);

    Optional<SubCategory> findByName(String name, TransactionContext context);

    SubCategory save(SubCategory subCategory, TransactionContext context);

    SubCategory update(SubCategory subCategory, TransactionContext context);

}
