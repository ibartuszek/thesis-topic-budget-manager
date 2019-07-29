package hu.elte.bm.transactionservice.domain.database;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.MainCategoryException;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryException;
import hu.elte.bm.transactionservice.domain.income.Income;
import hu.elte.bm.transactionservice.domain.income.IncomeException;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Component("databaseProxy")
public class DatabaseProxy implements DatabaseFacade {

    private static final String EXCEPTION_MESSAGE = "Unexpected error happens during execution. Please try again later.";

    private final DatabaseFacade databaseFacade;

    public DatabaseProxy(final DatabaseFacade databaseFacade) {
        this.databaseFacade = databaseFacade;
    }

    @Override
    public List<MainCategory> findAllMainCategory(final TransactionType transactionType) {
        try {
            return databaseFacade.findAllMainCategory(transactionType);
        } catch (DataAccessException exception) {
            throw new DatabaseException(EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public Optional<MainCategory> findMainCategoryById(final Long id) {
        try {
            return databaseFacade.findMainCategoryById(id);
        } catch (DataAccessException exception) {
            throw new DatabaseException(EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public Optional<MainCategory> findMainCategoryByName(final String name, final TransactionType transactionType) {
        try {
            return databaseFacade.findMainCategoryByName(name, transactionType);
        } catch (DataAccessException exception) {
            throw new DatabaseException(EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public Optional<MainCategory> saveMainCategory(final MainCategory mainCategory) {
        try {
            return databaseFacade.saveMainCategory(mainCategory);
        } catch (DataAccessException exception) {
            throw new MainCategoryException(mainCategory, EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public Optional<MainCategory> updateMainCategory(final MainCategory mainCategory) {
        try {
            return databaseFacade.updateMainCategory(mainCategory);
        } catch (DataAccessException exception) {
            throw new MainCategoryException(mainCategory, EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public List<SubCategory> findAllSubCategory(final TransactionType transactionType) {
        try {
            return databaseFacade.findAllSubCategory(transactionType);
        } catch (DataAccessException exception) {
            throw new DatabaseException(EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public Optional<SubCategory> findSubCategoryById(final Long id) {
        try {
            return databaseFacade.findSubCategoryById(id);
        } catch (DataAccessException exception) {
            throw new DatabaseException(EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public Optional<SubCategory> findSubCategoryByName(final String name, final TransactionType transactionType) {
        try {
            return databaseFacade.findSubCategoryByName(name, transactionType);
        } catch (DataAccessException exception) {
            throw new DatabaseException(EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public Optional<SubCategory> saveSubCategory(final SubCategory subCategory) {
        try {
            return databaseFacade.saveSubCategory(subCategory);
        } catch (DataAccessException exception) {
            throw new SubCategoryException(subCategory, EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public Optional<SubCategory> updateSubCategory(final SubCategory subCategory) {
        try {
            return databaseFacade.updateSubCategory(subCategory);
        } catch (DataAccessException exception) {
            throw new SubCategoryException(subCategory, EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public List<Income> getIncomeList(final LocalDate start, final LocalDate end) {
        try {
            return databaseFacade.getIncomeList(start, end);
        } catch (DataAccessException exception) {
            throw new DatabaseException(EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public Income getIncomeById(final Long id) {
        try {
            return databaseFacade.getIncomeById(id);
        } catch (DataAccessException exception) {
            throw new DatabaseException(EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public Income saveIncome(final Income income) {
        try {
            return databaseFacade.saveIncome(income);
        } catch (DataAccessException exception) {
            throw new IncomeException(income, EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public Income updateIncome(final Income income) {
        try {
            return databaseFacade.updateIncome(income);
        } catch (DataAccessException exception) {
            throw new IncomeException(income, EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public Income deleteIncome(final Income income) {
        try {
            return databaseFacade.deleteIncome(income);
        } catch (DataAccessException exception) {
            throw new IncomeException(income, EXCEPTION_MESSAGE, exception);
        }
    }
}
