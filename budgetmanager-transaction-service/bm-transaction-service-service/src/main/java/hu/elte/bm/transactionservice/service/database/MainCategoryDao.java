package hu.elte.bm.transactionservice.service.database;

import java.util.List;
import java.util.Optional;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

public interface MainCategoryDao {

    List<MainCategory> findAll(TransactionContext context);

    Optional<MainCategory> findById(Long id, TransactionContext context);

    Optional<MainCategory> findByName(String name, TransactionContext context);

    MainCategory save(MainCategory mainCategory, TransactionContext context);

    MainCategory update(MainCategory mainCategory, TransactionContext context);

}
