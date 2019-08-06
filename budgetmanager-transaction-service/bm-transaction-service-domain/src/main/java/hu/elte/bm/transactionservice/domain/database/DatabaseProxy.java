package hu.elte.bm.transactionservice.domain.database;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.MainCategoryException;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryException;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionException;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Component("databaseProxy")
@PropertySource("classpath:messages.properties")
public class DatabaseProxy {

    @Value("${database_proxy.unexpected_error_message}")
    private String exceptionMessage;

    private final DatabaseFacade databaseFacade;

    public DatabaseProxy(final DatabaseFacade databaseFacade) {
        this.databaseFacade = databaseFacade;
    }

    public List<MainCategory> findAllMainCategory(final TransactionType transactionType) {
        try {
            return databaseFacade.findAllMainCategory(transactionType);
        } catch (DataAccessException exception) {
            throw new DatabaseException(exceptionMessage, exception);
        }
    }

    public Optional<MainCategory> findMainCategoryById(final Long id) {
        try {
            return databaseFacade.findMainCategoryById(id);
        } catch (DataAccessException exception) {
            throw new DatabaseException(exceptionMessage, exception);
        }
    }

    public Optional<MainCategory> findMainCategoryByName(final String name, final TransactionType transactionType) {
        try {
            return databaseFacade.findMainCategoryByName(name, transactionType);
        } catch (DataAccessException exception) {
            throw new DatabaseException(exceptionMessage, exception);
        }
    }

    public Optional<MainCategory> saveMainCategory(final MainCategory mainCategory) {
        try {
            return databaseFacade.saveMainCategory(mainCategory);
        } catch (DataAccessException exception) {
            throw new MainCategoryException(mainCategory, exceptionMessage, exception);
        }
    }

    public Optional<MainCategory> updateMainCategory(final MainCategory mainCategory) {
        try {
            return databaseFacade.updateMainCategory(mainCategory);
        } catch (DataAccessException exception) {
            throw new MainCategoryException(mainCategory, exceptionMessage, exception);
        }
    }

    public List<SubCategory> findAllSubCategory(final TransactionType transactionType) {
        try {
            return databaseFacade.findAllSubCategory(transactionType);
        } catch (DataAccessException exception) {
            throw new DatabaseException(exceptionMessage, exception);
        }
    }

    public Optional<SubCategory> findSubCategoryById(final Long id) {
        try {
            return databaseFacade.findSubCategoryById(id);
        } catch (DataAccessException exception) {
            throw new DatabaseException(exceptionMessage, exception);
        }
    }

    public Optional<SubCategory> findSubCategoryByName(final String name, final TransactionType transactionType) {
        try {
            return databaseFacade.findSubCategoryByName(name, transactionType);
        } catch (DataAccessException exception) {
            throw new DatabaseException(exceptionMessage, exception);
        }
    }

    public Optional<SubCategory> saveSubCategory(final SubCategory subCategory) {
        try {
            return databaseFacade.saveSubCategory(subCategory);
        } catch (DataAccessException exception) {
            throw new SubCategoryException(subCategory, exceptionMessage, exception);
        }
    }

    public Optional<SubCategory> updateSubCategory(final SubCategory subCategory) {
        try {
            return databaseFacade.updateSubCategory(subCategory);
        } catch (DataAccessException exception) {
            throw new SubCategoryException(subCategory, exceptionMessage, exception);
        }
    }

    public List<Transaction> findAllTransaction(final LocalDate start, final LocalDate end, final TransactionType transactionType) {
        try {
            return databaseFacade.findAllTransaction(start, end, transactionType);
        } catch (DataAccessException exception) {
            throw new DatabaseException(exceptionMessage, exception);
        }
    }

    public Optional<Transaction> findTransactionById(final Long id, final TransactionType transactionType) {
        try {
            return databaseFacade.findTransactionById(id, transactionType);
        } catch (DataAccessException exception) {
            throw new DatabaseException(exceptionMessage, exception);
        }
    }

    public List<Transaction> findTransactionByTitle(final String title, final TransactionType transactionType) {
        try {
            return databaseFacade.findTransactionByTitle(title, transactionType);
        } catch (DataAccessException exception) {
            throw new DatabaseException(exceptionMessage, exception);
        }
    }

    public Optional<Transaction> saveTransaction(final Transaction transaction) {
        try {
            return databaseFacade.saveTransaction(transaction);
        } catch (DataAccessException exception) {
            throw new TransactionException(transaction, exceptionMessage, exception);
        }
    }

    public Optional<Transaction> updateTransaction(final Transaction transaction) {
        try {
            return databaseFacade.updateTransaction(transaction);
        } catch (DataAccessException exception) {
            throw new TransactionException(transaction, exceptionMessage, exception);
        }
    }

    public void deleteTransaction(final Transaction transaction) {
        try {
            databaseFacade.deleteTransaction(transaction);
        } catch (DataAccessException exception) {
            throw new TransactionException(transaction, exceptionMessage, exception);
        }
    }

}
