package hu.elte.bm.transactionservice.dal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.elte.bm.transactionservice.dal.categories.MainCategoryDao;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryDao;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.database.DatabaseFacade;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Service("databaseFacade")
public class DefaultDatabaseFacade implements DatabaseFacade {

    @Autowired
    private MainCategoryDao mainCategoryDao;

    @Autowired
    private SubCategoryDao subCategoryDao;

    @Override
    public List<MainCategory> findAllMainCategory(final TransactionType transactionType) {
        return mainCategoryDao.findAll(transactionType);
    }

    @Override
    public Optional<MainCategory> findMainCategoryById(final Long id) {
        return mainCategoryDao.findById(id);
    }

    @Override
    public Optional<MainCategory> findMainCategoryByName(final String name, final TransactionType transactionType) {
        return mainCategoryDao.findByName(name, transactionType);
    }

    @Override
    public Optional<MainCategory> saveMainCategory(final MainCategory mainCategory) {
        return mainCategoryDao.save(mainCategory);
    }

    @Override
    public Optional<MainCategory> updateMainCategory(final MainCategory mainCategory) {
        return mainCategoryDao.update(mainCategory);
    }

    @Override
    public List<SubCategory> findAllSubCategory(final TransactionType transactionType) {
        return subCategoryDao.findAll(transactionType);
    }

    @Override
    public Optional<SubCategory> findSubCategoryById(final Long id) {
        return subCategoryDao.findById(id);
    }

    @Override
    public Optional<SubCategory> findSubCategoryByName(final String name, final TransactionType transactionType) {
        return subCategoryDao.findByName(name, transactionType);
    }

    @Override
    public Optional<SubCategory> saveSubCategory(final SubCategory subCategory) {
        return subCategoryDao.save(subCategory);
    }

    @Override
    public Optional<SubCategory> updateSubCategory(final SubCategory subCategory) {
        return subCategoryDao.update(subCategory);
    }

    @Override
    public List<Transaction> findAllTransaction(final LocalDate start, final LocalDate end, final TransactionType transactionType) {
        return null;
    }

    @Override
    public Optional<Transaction> findTransactionById(final Long id) {
        return Optional.empty();
    }

    @Override
    public List<Transaction> findTransactionByTitle(final String name, final TransactionType transactionType) {
        return null;
    }

    @Override
    public Optional<Transaction> saveTransaction(final Transaction transaction) {
        return Optional.empty();
    }

    @Override
    public Optional<Transaction> updateTransaction(final Transaction transaction) {
        return Optional.empty();
    }

    @Override
    public Optional<Transaction> deleteTransaction(final Transaction transaction) {
        return Optional.empty();
    }

}
