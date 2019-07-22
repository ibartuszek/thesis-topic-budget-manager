package hu.elte.bm.transactionservice.domain.database;

import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import hu.elte.bm.transactionservice.domain.categories.CategoryType;
import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.categories.MainCategoryException;
import hu.elte.bm.transactionservice.domain.categories.SubCategory;
import hu.elte.bm.transactionservice.domain.categories.SubCategoryException;
import hu.elte.bm.transactionservice.domain.income.Income;
import hu.elte.bm.transactionservice.domain.income.IncomeException;

@Service
public class DatabaseProxy implements DatabaseFacade {

    private static final String EXCEPTION_MESSAGE = "Unexpected error happens during execution. Please try again later.";

    private final DatabaseFacade databaseFacade;

    public DatabaseProxy(DatabaseFacade databaseFacade) {
        this.databaseFacade = databaseFacade;
    }

    @Override
    public List<MainCategory> findAllMainCategory(final CategoryType categoryType) {
        try {
            return databaseFacade.findAllMainCategory(categoryType);
        } catch (DataAccessException exception) {
            throw new DatabaseException(EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public MainCategory saveMainCategory(final MainCategory mainCategory) {
        try {
            return databaseFacade.saveMainCategory(mainCategory);
        } catch (DataAccessException exception) {
            throw new MainCategoryException(mainCategory, EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public MainCategory updateMainCategory(final MainCategory mainCategory) {
        try {
            return databaseFacade.updateMainCategory(mainCategory);
        } catch (DataAccessException exception) {
            throw new MainCategoryException(mainCategory, EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public List<SubCategory> findAllSubCategory(final CategoryType categoryType) {
        try {
            return databaseFacade.findAllSubCategory(categoryType);
        } catch (DataAccessException exception) {
            throw new DatabaseException(EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public SubCategory saveSubCategory(final SubCategory subCategory) {
        try {
            return databaseFacade.saveSubCategory(subCategory);
        } catch (DataAccessException exception) {
            throw new SubCategoryException(subCategory, EXCEPTION_MESSAGE, exception);
        }
    }

    @Override
    public SubCategory updateSubCategory(final SubCategory subCategory) {
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
