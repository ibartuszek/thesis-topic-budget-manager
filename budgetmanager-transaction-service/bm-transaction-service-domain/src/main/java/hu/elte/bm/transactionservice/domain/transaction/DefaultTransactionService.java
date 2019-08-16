package hu.elte.bm.transactionservice.domain.transaction;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.database.DatabaseProxy;

@Service("transactionService")
public class DefaultTransactionService implements TransactionService {

    private final DatabaseProxy dataBaseProxy;

    @Value("${transaction.days_to_subtract_to_calculate_first_day_of_new_period}")
    private final long daysToSubtract = 30L;

    @Value("${transaction.transaction_cannot_be_null}")
    private String cannotBeNullExceptionMessage;

    @Value("${transaction.main_category_id_cannot_be_null}")
    private String mainCategoryIdExceptionMessage;

    @Value("${transaction.sub_category_id_cannot_be_null}")
    private String subCategoryIdExceptionMessage;

    @Value("${transaction.date_before_the_beginning}")
    private String dateBeforeThePeriodExceptionMessage;

    @Value("${transaction.transaction_cannot_be_found}")
    private String originalTransactionCannotBeFoundExceptionMessage;

    @Value("${transaction.transaction_type_cannot_be_changed}")
    private String transactionTypeCannotBeChangedExceptionMessage;

    @Value("${transaction.transaction_is_locked}")
    private String transactionIsLockedExceptionMessage;

    DefaultTransactionService(final DatabaseProxy dataBaseProxy) {
        this.dataBaseProxy = dataBaseProxy;
    }

    @Override
    public List<Transaction> findAllTransaction(final LocalDate start, final LocalDate end, final TransactionType transactionType) {
        Assert.notNull(start, MessageFormat.format(cannotBeNullExceptionMessage, "start"));
        Assert.notNull(end, MessageFormat.format(cannotBeNullExceptionMessage, "end"));
        Assert.notNull(transactionType, MessageFormat.format(cannotBeNullExceptionMessage, "transactionType"));
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
    public Optional<Transaction> update(final Transaction transaction, final TransactionType transactionType) {
        validateForUpdate(transaction, transactionType);
        return isSavable(transaction) ? dataBaseProxy.updateTransaction(transaction) : Optional.empty();
    }

    @Override
    public Optional<Transaction> delete(final Transaction transaction, final TransactionType transactionType) {
        validateForUpdate(transaction, transactionType);
        Optional<Transaction> result;
        dataBaseProxy.deleteTransaction(transaction);
        result = Optional.of(transaction);
        return result;
    }

    @Override
    public LocalDate getTheFirstDateOfTheNewPeriod(final TransactionType type) {
        return dataBaseProxy.findAllTransaction(LocalDate.now().minusDays(daysToSubtract), LocalDate.now(), type).stream()
            .filter(Transaction::isLocked)
            .max(Comparator.comparing(Transaction::getDate))
            .map(Transaction::getDate)
            .map(localDate -> localDate.plusDays(1L))
            .orElseGet(() -> LocalDate.now().minusDays(daysToSubtract));
    }

    private void validate(final Transaction transaction) {
        Assert.notNull(transaction, MessageFormat.format(cannotBeNullExceptionMessage, "transaction"));
        if (transaction.getMainCategory().getId() == null) {
            throw new TransactionException(transaction, mainCategoryIdExceptionMessage);
        } else if (!hasValidSubCategories(transaction.getMainCategory())) {
            throw new TransactionException(transaction, subCategoryIdExceptionMessage);
        } else if (transaction.getSubCategory() != null && transaction.getSubCategory().getId() == null) {
            throw new TransactionException(transaction, subCategoryIdExceptionMessage);
        }
    }

    private boolean hasValidSubCategories(final MainCategory mainCategory) {
        return mainCategory.getSubCategorySet().stream()
            .noneMatch(subCategory -> subCategory.getId() == null);
    }

    private boolean isSavable(final Transaction transaction) {
        List<Transaction> transactionList = dataBaseProxy.findTransactionByTitle(transaction.getTitle(), transaction.getTransactionType());
        return transactionList.stream()
            .filter(t1 -> t1.getDate().equals(transaction.getDate()))
            .filter(Predicate.not(Transaction::isLocked))
            .findAny().isEmpty();
    }

    private void validateForUpdate(final Transaction transaction, final TransactionType transactionType) {
        validate(transaction);
        Transaction transactionFromRepository = dataBaseProxy.findTransactionById(transaction.getId(), transactionType).orElse(null);
        if (transactionFromRepository == null) {
            throw new TransactionException(transaction, originalTransactionCannotBeFoundExceptionMessage);
        } else if (transactionFromRepository.isLocked()) {
            throw new TransactionException(transaction, transactionIsLockedExceptionMessage);
        } else if (transaction.getTransactionType() != transactionFromRepository.getTransactionType()) {
            throw new TransactionException(transaction, transactionTypeCannotBeChangedExceptionMessage);
        }
    }

}
