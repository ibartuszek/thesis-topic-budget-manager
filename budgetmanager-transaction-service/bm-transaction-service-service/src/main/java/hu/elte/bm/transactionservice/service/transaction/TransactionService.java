package hu.elte.bm.transactionservice.service.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.categories.MainCategory;
import hu.elte.bm.transactionservice.domain.exceptions.transaction.IllegalTransactionException;
import hu.elte.bm.transactionservice.domain.exceptions.transaction.TransactionConflictException;
import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.service.database.TransactionDaoProxy;

@Service("transactionService")
public class TransactionService {

    private final TransactionDaoProxy transactionDaoProxy;
    private final TransactionDateValidator dateValidator;

    @Value("${transaction.beginning_of_the_search_cannot_be_null}")
    private String beginningOfSearchCannotBeNull;

    @Value("${transaction.main_category_id_cannot_be_null}")
    private String mainCategoryCannotBeNull;

    @Value("${transaction.transaction_cannot_be_found}")
    private String originalTransactionCannotBeFound;

    @Value("${transaction.sub_category_id_cannot_be_null}")
    private String subCategoryIdCannotBeNull;

    @Value("${transaction.transaction_cannot_be_null}")
    private String transactionCannotBeNull;

    @Value("${transaction.transaction_id_cannot_be_null}")
    private String transactionIdCannotBeNull;

    @Value("${transaction.transaction_id_must_be_null}")
    private String transactionIdMustBeNull;

    @Value("${transaction.transaction_is_locked_delete}")
    private String transactionIsLockedForDelete;

    @Value("${transaction.transaction_is_locked_update}")
    private String transactionIsLockedExceptionForUpdate;

    @Value("${transaction.transaction_has_been_saved_before}")
    private String transactionHasBeenSavedBefore;

    @Value("${transaction.transaction_not_changed}")
    private String transactionNotChanged;

    @Value("${transaction.transaction_type_cannot_be_changed}")
    private String typeCannotBeChange;

    @Value("${transaction.category_type_cannot_be_null}")
    private String typeCannotBeNull;

    @Value("${transaction.user_id_cannot_be_null}")
    private String userIdCannotBeNull;

    TransactionService(final TransactionDaoProxy transactionDaoProxy, final TransactionDateValidator dateValidator) {
        this.transactionDaoProxy = transactionDaoProxy;
        this.dateValidator = dateValidator;
    }

    public List<Transaction> getTransactionList(final LocalDate start, final LocalDate end, final TransactionContext context) {
        validate(context);
        Assert.notNull(start, beginningOfSearchCannotBeNull);
        LocalDate finish = end != null ? end : LocalDate.now();
        return transactionDaoProxy.getTransactionList(start, finish, context);
    }

    public Transaction save(final Transaction transaction, final TransactionContext context) {
        validate(context);
        validateForSave(transaction, context);
        return transactionDaoProxy.save(transaction, context);
    }

    public Transaction update(final Transaction transaction, final TransactionContext context) {
        validate(context);
        validateForUpdate(transaction, context);
        return transactionDaoProxy.update(transaction, context);
    }

    public Transaction delete(final Transaction transaction, final TransactionContext context) {
        validate(context);
        validateForDelete(transaction, context);
        return transactionDaoProxy.delete(transaction, context);
    }

    public LocalDate getTheFirstDateOfTheNewPeriod(final Long userId) {
        return dateValidator.getTheFirstDateOfTheNewPeriod(userId);
    }

    private void validate(final TransactionContext context) {
        Assert.notNull(context.getTransactionType(), typeCannotBeNull);
        Assert.notNull(context.getUserId(), userIdCannotBeNull);
    }

    private void validateForSave(final Transaction transaction, final TransactionContext context) {
        Assert.notNull(transaction, transactionCannotBeNull);
        Assert.isNull(transaction.getId(), transactionIdMustBeNull);
        validateFields(transaction, context);
        transactionIsNotReserved(transaction, context);
    }

    private void validateFields(final Transaction transaction, final TransactionContext context) {
        if (transaction.getMainCategory().getId() == null) {
            throw new IllegalTransactionException(transaction, mainCategoryCannotBeNull);
        } else if (!hasValidSubCategories(transaction.getMainCategory())) {
            throw new IllegalTransactionException(transaction, subCategoryIdCannotBeNull);
        } else if (transaction.getSubCategory() != null && transaction.getSubCategory().getId() == null) {
            throw new IllegalTransactionException(transaction, subCategoryIdCannotBeNull);
        }
        dateValidator.validate(transaction, context);
    }

    private boolean hasValidSubCategories(final MainCategory mainCategory) {
        return mainCategory.getSubCategorySet().stream()
            .noneMatch(subCategory -> subCategory.getId() == null);
    }

    private void transactionIsNotReserved(final Transaction transaction, final TransactionContext context) {
        List<Transaction> transactionList = transactionDaoProxy.findByTitle(transaction.getTitle(), context);
        if (!isSavable(transactionList, transaction)) {
            throw new TransactionConflictException(transaction, transactionHasBeenSavedBefore);
        }
    }

    private boolean isSavable(final List<Transaction> transactionList, final Transaction transaction) {
        return transactionList.stream()
            .filter(t1 -> !t1.getId().equals(transaction.getId()))
            .filter(t1 -> t1.getDate().equals(transaction.getDate()))
            .filter(t1 -> t1.getMainCategory().equals(transaction.getMainCategory()))
            .filter(Predicate.not(Transaction::isLocked))
            .findAny().isEmpty();
    }

    private void validateForUpdate(final Transaction transaction, final TransactionContext context) {
        validateTransactionWithIdForUpdate(transaction);
        validateFields(transaction, context);
        Transaction originalTransaction = getOriginalTransaction(transaction, context);
        validateTransactionIsNotLocked(transaction, originalTransaction, transactionIsLockedExceptionForUpdate);
        validateAgainstOriginalTransaction(transaction, originalTransaction);
        transactionIsNotReserved(transaction, context);
    }

    private void validateTransactionWithIdForUpdate(final Transaction transaction) {
        Assert.notNull(transaction, transactionCannotBeNull);
        Assert.notNull(transaction.getId(), transactionIdCannotBeNull);
    }

    private Transaction getOriginalTransaction(final Transaction transaction, final TransactionContext context) {
        Optional<Transaction> transactionFromRepository = transactionDaoProxy.findById(transaction.getId(), context);
        if (transactionFromRepository.isEmpty()) {
            throw new IllegalTransactionException(transaction, originalTransactionCannotBeFound);
        }
        return transactionFromRepository.get();
    }

    private void validateTransactionIsNotLocked(final Transaction transaction, final Transaction originalTransaction, final String lockedMessage) {
        if (originalTransaction.isLocked()) {
            throw new IllegalTransactionException(transaction, lockedMessage);
        }
    }

    private void validateAgainstOriginalTransaction(final Transaction transaction, final Transaction originalTransaction) {
        if (transaction.getTransactionType() != originalTransaction.getTransactionType()) {
            throw new IllegalTransactionException(transaction, typeCannotBeChange);
        } else if (transaction.equals(originalTransaction)) {
            throw new IllegalTransactionException(transaction, transactionNotChanged);
        }
    }

    private void validateForDelete(final Transaction transaction, final TransactionContext context) {
        validateTransactionWithIdForUpdate(transaction);
        Transaction originalTransaction = getOriginalTransaction(transaction, context);
        validateTransactionIsNotLocked(transaction, originalTransaction, transactionIsLockedForDelete);
    }

}
