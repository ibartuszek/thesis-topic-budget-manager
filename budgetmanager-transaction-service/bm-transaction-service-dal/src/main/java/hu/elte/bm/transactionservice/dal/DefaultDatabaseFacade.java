package hu.elte.bm.transactionservice.dal;

import static hu.elte.bm.transactionservice.domain.transaction.TransactionType.INCOME;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.elte.bm.transactionservice.dal.categories.MainCategoryDao;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryDao;
import hu.elte.bm.transactionservice.dal.transaction.IncomeDao;
import hu.elte.bm.transactionservice.dal.transaction.OutcomeDao;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.service.database.DatabaseFacade;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

@Service("databaseFacade")
public class DefaultDatabaseFacade implements DatabaseFacade {

    @Autowired
    private MainCategoryDao mainCategoryDao;

    @Autowired
    private SubCategoryDao subCategoryDao;

    @Autowired
    private IncomeDao incomeDao;

    @Autowired
    private OutcomeDao outcomeDao;

    @Override
    public List<MainCategory> findAllMainCategory(final TransactionContext context) {
        return mainCategoryDao.findAll(context);
    }

    @Override
    public Optional<MainCategory> findMainCategoryById(final Long id, final TransactionContext context) {
        return mainCategoryDao.findById(id, context);
    }

    @Override
    public Optional<MainCategory> findMainCategoryByName(final String name, final TransactionContext context) {
        return mainCategoryDao.findByName(name, context);
    }

    @Override
    public Optional<MainCategory> saveMainCategory(final MainCategory mainCategory, final TransactionContext context) {
        return mainCategoryDao.save(mainCategory, context);
    }

    @Override
    public Optional<MainCategory> updateMainCategory(final MainCategory mainCategory, final TransactionContext context) {
        return mainCategoryDao.update(mainCategory, context);
    }

    @Override
    public List<SubCategory> findAllSubCategory(final TransactionContext context) {
        return subCategoryDao.findAll(context);
    }

    @Override
    public Optional<SubCategory> findSubCategoryById(final Long id, final TransactionContext context) {
        return subCategoryDao.findById(id, context);
    }

    @Override
    public Optional<SubCategory> findSubCategoryByName(final String name, final TransactionContext context) {
        return subCategoryDao.findByName(name, context);
    }

    @Override
    public Optional<SubCategory> saveSubCategory(final SubCategory subCategory, final TransactionContext context) {
        return subCategoryDao.save(subCategory, context);
    }

    @Override
    public Optional<SubCategory> updateSubCategory(final SubCategory subCategory, final TransactionContext context) {
        return subCategoryDao.update(subCategory, context);
    }

    @Override
    public List<Transaction> findAllTransaction(final LocalDate start, final LocalDate end, final TransactionContext context) {
        return context.getTransactionType() == INCOME ? incomeDao.findAll(start, end, context) : outcomeDao.findAll(start, end, context);
    }

    @Override
    public Optional<Transaction> findTransactionById(final Long id, final TransactionContext context) {
        return context.getTransactionType() == INCOME ? incomeDao.findById(id, context) : outcomeDao.findById(id, context);
    }

    @Override
    public List<Transaction> findTransactionByTitle(final String title, final TransactionContext context) {
        return context.getTransactionType() == INCOME ? incomeDao.findByTitle(title, context) : outcomeDao.findByTitle(title, context);
    }

    @Override
    public Optional<Transaction> saveTransaction(final Transaction transaction, final TransactionContext context) {
        return context.getTransactionType() == INCOME ? incomeDao.save(transaction, context) : outcomeDao.save(transaction, context);
    }

    @Override
    public Optional<Transaction> updateTransaction(final Transaction transaction, final TransactionContext context) {
        return context.getTransactionType() == INCOME ? incomeDao.update(transaction, context) : outcomeDao.update(transaction, context);
    }

    @Override
    public void deleteTransaction(final Transaction transaction, final TransactionContext context) {
        if (context.getTransactionType() == INCOME) {
            incomeDao.delete(transaction, context);
        } else {
            outcomeDao.delete(transaction, context);
        }
    }

}
