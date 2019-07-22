package hu.elte.bm.transactionservice.domain.income;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;

@Service("incomeService")
public class DefaultIncomeService implements IncomeService {

    private static final String CANNOT_BE_NULL_EXCEPTION_MESSAGE = "{0} cannot be null!";
    private final DatabaseProxy dataBaseProxy;

    DefaultIncomeService(final DatabaseProxy dataBaseProxy) {
        this.dataBaseProxy = dataBaseProxy;
    }

    @Override
    public List<Income> getIncomeList(final LocalDate start, final LocalDate end) {
        Assert.notNull(start, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "start"));
        Assert.notNull(end, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "end"));
        return dataBaseProxy.getIncomeList(start, end);
    }

    @Override
    public List<Income> getIncomeList(final LocalDate start) {
        return getIncomeList(start, LocalDate.now());
    }

    @Override
    public Optional<Income> saveIncome(final Income income) {
        Assert.notNull(income, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "income"));
        Optional<Income> result = Optional.empty();
        List<Income> incomeList = getIncomeList(LocalDate.now());
        if (!hasBeenSaved(income, incomeList)) {
            result = Optional.of(dataBaseProxy.saveIncome(income));
        }
        return result;
    }

    @Override
    public Optional<Income> updateIncome(final Income income) {
        Assert.notNull(income, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "income"));
        Optional<Income> result = Optional.empty();
        Income incomeFromRepository = dataBaseProxy.getIncomeById(income.getId());
        if (canBeModified(incomeFromRepository)) {
            result = Optional.of(dataBaseProxy.updateIncome(income));
        }
        return result;
    }

    @Override
    public Optional<Income> deleteIncome(final Income income) {
        Assert.notNull(income, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "income"));
        Optional<Income> result = Optional.empty();
        Income incomeFromRepository = dataBaseProxy.getIncomeById(income.getId());
        if (canBeModified(incomeFromRepository)) {
            result = Optional.of(dataBaseProxy.deleteIncome(income));
        }
        return result;
    }

    private boolean hasBeenSaved(Income income, List<Income> incomeList) {
        return incomeList.stream().anyMatch(income::equals);
    }

    private boolean canBeModified(Income incomeFromRepository) {
        return incomeFromRepository != null && !incomeFromRepository.isLocked();
    }
}
