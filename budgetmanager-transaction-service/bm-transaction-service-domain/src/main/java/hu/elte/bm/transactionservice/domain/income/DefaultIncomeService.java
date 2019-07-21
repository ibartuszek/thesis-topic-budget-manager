package hu.elte.bm.transactionservice.domain.income;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.DatabaseFacade;

@Service("incomeService")
public class DefaultIncomeService implements IncomeService {

    private static final String CANNOT_BE_NULL_EXCEPTION_MESSAGE = "{} cannot be null!";
    private static final String INCOME_EXCEPTION_MESSAGE = "Unexpected error happens during execution. Please try again later.";
    private final DatabaseFacade databaseFacade;

    DefaultIncomeService(final DatabaseFacade databaseFacade) {
        this.databaseFacade = databaseFacade;
    }

    @Override
    public List<Income> getIncomeList(final LocalDate start, final LocalDate end) {
        Assert.notNull(end, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "end"));
        return databaseFacade.getIncomeList(start, end);
    }

    @Override
    public List<Income> getIncomeList(final LocalDate start) {
        Assert.notNull(start, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "start"));
        return databaseFacade.getIncomeList(start, LocalDate.now());
    }

    @Override
    public Optional<Income> saveIncome(final Income income) throws IncomeException {
        Assert.notNull(income, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "income"));
        Optional<Income> result = Optional.empty();
        List<Income> incomeList = getIncomeList(LocalDate.now());
        if (!hasBeenSaved(income, incomeList)) {
            try {
                result = Optional.of(databaseFacade.saveIncome(income));
            } catch (DataAccessException exception) {
                throw new IncomeException(income, INCOME_EXCEPTION_MESSAGE, exception);
            }
        }
        return result;
    }

    @Override
    public Optional<Income> updateIncome(final Income income) throws IncomeException {
        Assert.notNull(income, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "income"));
        Optional<Income> result = Optional.empty();
        Income incomeFromRepository = databaseFacade.getIncomeById(income.getId());
        if (!incomeFromRepository.isLockedPeriod()) {
            try {
                result = Optional.of(databaseFacade.updateIncome(income));
            } catch (DataAccessException exception) {
                throw new IncomeException(income, INCOME_EXCEPTION_MESSAGE, exception);
            }
        }
        return result;
    }

    @Override
    public Optional<Income> deleteIncome(final Income income) throws IncomeException {
        Assert.notNull(income, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "income"));
        Optional<Income> result = Optional.empty();
        Income incomeFromRepository = databaseFacade.getIncomeById(income.getId());
        if (!incomeFromRepository.isLockedPeriod()) {
            try {
                result = Optional.of(databaseFacade.deleteIncome(income));
            } catch (DataAccessException exception) {
                throw new IncomeException(income, INCOME_EXCEPTION_MESSAGE, exception);
            }
        }
        return result;
    }

    private boolean hasBeenSaved(Income income, List<Income> incomeList) {
        return incomeList.stream().anyMatch(income::equals);
    }
}
