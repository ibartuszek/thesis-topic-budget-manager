package hu.elte.bm.transactionservice.domain.transaction;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;

@Service("transactionService")
public class DefaultTransactionService implements TransactionService {

    private static final long DAYS_TO_SUBTRACT = 30L;
    private static final String CANNOT_BE_NULL_EXCEPTION_MESSAGE = "{0} cannot be null!";
    private static final String MAIN_CATEGORY_ID_EXCEPTION_MESSAGE = "The Id of mainCategory cannot be null!";
    private static final String SUB_CATEGORY_ID_EXCEPTION_MESSAGE = "The Id of subCategory cannot be null!";
    private static final String DATE_BEFORE_THE_PERIOD_EXCEPTION_MESSAGE = "The date of transaction cannot be before the beginning of the period!";
    private static final String ORIGINAL_TRANSACTION_CANNOT_BE_FOUND_EXCEPTION_MESSAGE = "Original transaction cannot be found in the repository!";
    private static final String TRANSACTION_TYPE_CANNOT_BE_CHANGED_EXCEPTION_MESSAGE = "Transaction type cannot be changed!";

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
        validate(transaction);
        return isSavable(transaction) ? dataBaseProxy.saveTransaction(transaction) : Optional.empty();
    }

    @Override
    public Optional<Transaction> update(final Transaction transaction) {
        validateForUpdate(transaction);
        return isSavable(transaction) ? dataBaseProxy.updateTransaction(transaction) : Optional.empty();
    }

    @Override
    public Optional<Transaction> delete(final Transaction transaction) {
        validateForUpdate(transaction);
        return isSavable(transaction) ? dataBaseProxy.deleteTransaction(transaction) : Optional.empty();
    }

    private void validate(final Transaction transaction) {
        Assert.notNull(transaction, MessageFormat.format(CANNOT_BE_NULL_EXCEPTION_MESSAGE, "transaction"));
        LocalDate endOfTheLastPeriod = getLastDateOfThePeriod(transaction)
            .orElse(LocalDate.now().minusDays(DAYS_TO_SUBTRACT));
        if (transaction.getMainCategory().getId() == null) {
            throw new TransactionException(transaction, MAIN_CATEGORY_ID_EXCEPTION_MESSAGE);
        } else if (!hasValidSubCategories(transaction.getMainCategory())) {
            throw new TransactionException(transaction, SUB_CATEGORY_ID_EXCEPTION_MESSAGE);
        } else if (transaction.getDate().isBefore(endOfTheLastPeriod)) {
            throw new TransactionException(transaction, DATE_BEFORE_THE_PERIOD_EXCEPTION_MESSAGE);
        }
    }

    private boolean hasValidSubCategories(final MainCategory mainCategory) {
        return mainCategory.getSubCategorySet().stream()
            .noneMatch(subCategory -> subCategory.getId() == null);
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

    private void validateForUpdate(final Transaction transaction) {
        validate(transaction);
        Transaction transactionFromRepository = dataBaseProxy.findTransactionById(transaction.getId()).orElse(null);
        if (transactionFromRepository == null) {
            throw new TransactionException(transaction, MAIN_CATEGORY_ID_EXCEPTION_MESSAGE);
        } else if (transactionFromRepository.isLocked()) {
            throw new TransactionException(transaction, ORIGINAL_TRANSACTION_CANNOT_BE_FOUND_EXCEPTION_MESSAGE);
        } else if (transaction.getTransactionType() != transactionFromRepository.getTransactionType()) {
            throw new TransactionException(transaction, TRANSACTION_TYPE_CANNOT_BE_CHANGED_EXCEPTION_MESSAGE);
        }
    }

}
