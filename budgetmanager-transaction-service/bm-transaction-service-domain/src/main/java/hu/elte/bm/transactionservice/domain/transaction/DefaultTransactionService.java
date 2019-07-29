package hu.elte.bm.transactionservice.domain.transaction;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;

@Service("transactionService")
public class DefaultTransactionService implements TransactionService {

    private static final String CANNOT_BE_NULL_EXCEPTION_MESSAGE = "{0} cannot be null!";
    private static final long DAYS_TO_SUBTRACT = 30L;

    private final DatabaseProxy dataBaseProxy;

    DefaultTransactionService(final DatabaseProxy dataBaseProxy) {
        this.dataBaseProxy = dataBaseProxy;
    }

    @Override
    public List<Transaction> findAllTransaction(final LocalDate start, final LocalDate end, final TransactionType transactionType) {
        Assert.notNull(start, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "start"));
        Assert.notNull(end, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "end"));
        Assert.notNull(transactionType, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "transactionType"));
        return dataBaseProxy.findAllTransaction(start, end, transactionType);
    }

    @Override
    public List<Transaction> findAllTransaction(final LocalDate start, final TransactionType transactionType) {
        return findAllTransaction(start, LocalDate.now(), transactionType);
    }

    @Override
    public Optional<Transaction> save(final Transaction transaction) {
        Assert.notNull(transaction, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "transaction"));
        return isValid(transaction) && isSavable(transaction) ? dataBaseProxy.saveTransaction(transaction) : Optional.empty();
    }

    @Override
    public Optional<Transaction> update(final Transaction transaction) {
        Assert.notNull(transaction, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "transaction"));
        return isValid(transaction) && isUpdatable(transaction) ? dataBaseProxy.updateTransaction(transaction) : Optional.empty();
    }

    @Override
    public Optional<Transaction> delete(final Transaction transaction) {
        Assert.notNull(transaction, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "transaction"));
        return Optional.empty();
    }

    private boolean isValid(final Transaction transaction) {
        LocalDate endOfTheLastPeriod = getLastDateOfThePeriod(transaction)
            .orElse(LocalDate.now().minusDays(DAYS_TO_SUBTRACT));
        return transaction.getMainCategory().getId() != null
            && transaction.getMainCategory().getSubCategorySet().stream()
            .noneMatch(subCategory -> subCategory.getId() == null)
            && transaction.getDate().isAfter(endOfTheLastPeriod);
    }

    private Optional<LocalDate> getLastDateOfThePeriod(final Transaction transaction) {
        return dataBaseProxy.findAllTransaction(LocalDate.now().minusDays(DAYS_TO_SUBTRACT), LocalDate.now(), transaction.getTransactionType()).stream()
            .filter(Transaction::isLocked)
            .max(Comparator.comparing(Transaction::getDate))
            .map(Transaction::getDate);
    }

    private boolean isSavable(final Transaction transaction) {
        List<Transaction> transactionList = dataBaseProxy.findTransactionByTitle(transaction.getTitle(), transaction.getTransactionType());
        return transactionList.stream()
            .filter(Predicate.not(Transaction::isLocked))
            .filter(t1 -> t1.getDate().equals(transaction.getDate()))
            .findAny().isEmpty();
    }

    private boolean isUpdatable(final Transaction transaction) {
        Transaction transactionFromRepository = dataBaseProxy.findTransactionById(transaction.getId()).orElse(null);
        return transactionFromRepository != null
            && !transactionFromRepository.isLocked()
            && transactionFromRepository.getTransactionType() == transaction.getTransactionType()
            && isSavable(transaction);
    }
}
