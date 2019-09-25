package hu.elte.bm.transactionservice.service.database;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

public interface DatabaseFacade {

    List<MainCategory> findAllMainCategory(TransactionContext context);

    Optional<MainCategory> findMainCategoryById(Long id, TransactionContext context);

    Optional<MainCategory> findMainCategoryByName(String name, TransactionContext context);

    Optional<MainCategory> saveMainCategory(MainCategory mainCategory, TransactionContext context);

    Optional<MainCategory> updateMainCategory(MainCategory mainCategory, TransactionContext context);

    List<SubCategory> findAllSubCategory(TransactionContext context);

    Optional<SubCategory> findSubCategoryById(Long id, TransactionContext context);

    Optional<SubCategory> findSubCategoryByName(String name, TransactionContext context);

    Optional<SubCategory> saveSubCategory(SubCategory subCategory, TransactionContext context);

    Optional<SubCategory> updateSubCategory(SubCategory subCategory, TransactionContext context);

    List<Transaction> findAllTransaction(LocalDate start, LocalDate end, TransactionContext context);

    Optional<Transaction> findTransactionById(Long id, TransactionContext context);

    List<Transaction> findTransactionByTitle(String title, TransactionContext context);

    Optional<Transaction> saveTransaction(Transaction transaction, TransactionContext context);

    Optional<Transaction> updateTransaction(Transaction transaction, TransactionContext context);

    void deleteTransaction(Transaction transaction, TransactionContext context);

}
