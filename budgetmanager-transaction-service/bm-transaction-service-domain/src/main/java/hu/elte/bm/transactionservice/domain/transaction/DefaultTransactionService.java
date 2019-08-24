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

    @Value("${transaction.transaction_is_locked_update}")
    private String transactionIsLockedExceptionMessageForUpdate;

    @Value("${transaction.transaction_is_locked_delete}")
    private String transactionIsLockedExceptionMessageForDelete;

    DefaultTransactionService(final DatabaseProxy dataBaseProxy) {
        this.dataBaseProxy = dataBaseProxy;
    }

    @Override
    public List<Transaction> findAllTransaction(final LocalDate start, final LocalDate end, final TransactionContext context) {
        validate(context);
        Assert.notNull(start, MessageFormat.format(cannotBeNullExceptionMessage, "start"));
        LocalDate finish = end != null ? end : LocalDate.now();
        return dataBaseProxy.findAllTransaction(start, finish, context);
    }

    @Override
    public Optional<Transaction> save(final Transaction transaction, final TransactionContext context) {
        validate(context);
        validate(transaction);
        return isSavable(transaction, context) ? dataBaseProxy.saveTransaction(transaction, context) : Optional.empty();
    }

    @Override
    public Optional<Transaction> update(final Transaction transaction, final TransactionContext context) {
        validate(context);
        validateForUpdate(transaction, context, transactionIsLockedExceptionMessageForUpdate);
        return isSavable(transaction, context) ? dataBaseProxy.updateTransaction(transaction, context) : Optional.empty();
    }

    @Override
    public Optional<Transaction> delete(final Transaction transaction, final TransactionContext context) {
        validateForUpdate(transaction, context, transactionIsLockedExceptionMessageForDelete);
        dataBaseProxy.deleteTransaction(transaction, context);
        return Optional.of(transaction);
    }

    @Override
    public LocalDate getTheFirstDateOfTheNewPeriod(final TransactionContext context) {
        return dataBaseProxy.findAllTransaction(LocalDate.now().minusDays(daysToSubtract), LocalDate.now(), context).stream()
            .filter(Transaction::isLocked)
            .max(Comparator.comparing(Transaction::getDate))
            .map(Transaction::getDate)
            .map(localDate -> localDate.plusDays(1L))
            .orElseGet(() -> LocalDate.now().minusDays(daysToSubtract));
    }

    private void validate(final TransactionContext context) {
        Assert.notNull(context.getTransactionType(), MessageFormat.format(cannotBeNullExceptionMessage, "transactionType"));
        Assert.notNull(context.getUserId(), MessageFormat.format(cannotBeNullExceptionMessage, "userId"));
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

    private boolean isSavable(final Transaction transaction, final TransactionContext context) {
        List<Transaction> transactionList = dataBaseProxy.findTransactionByTitle(transaction.getTitle(), context);
        return transactionList.stream()
            .filter(t1 -> t1.getDate().equals(transaction.getDate()))
            .filter(t1 -> t1.getMainCategory().equals(transaction.getMainCategory()))
            .filter(Predicate.not(Transaction::isLocked))
            .findAny().isEmpty();
    }

    private void validateForUpdate(final Transaction transaction, final TransactionContext context,
        final String lockedExceptionMessage) {
        validate(transaction);
        Transaction transactionFromRepository = dataBaseProxy.findTransactionById(transaction.getId(), context).orElse(null);
        if (transactionFromRepository == null) {
            throw new TransactionException(transaction, originalTransactionCannotBeFoundExceptionMessage);
        } else if (transactionFromRepository.isLocked()) {
            throw new TransactionException(transaction, lockedExceptionMessage);
        } else if (transaction.getTransactionType() != transactionFromRepository.getTransactionType()) {
            throw new TransactionException(transaction, transactionTypeCannotBeChangedExceptionMessage);
        }
    }

}
