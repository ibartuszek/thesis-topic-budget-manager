package hu.elte.bm.transactionservice.web.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.commonpack.validator.ModelValidator;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

@Service
public class TransactionModelService {

    private final ModelValidator validator;
    private final TransactionService transactionService;
    private final TransactionModelTransformer transformer;



    @Value("${transaction.transaction_is_invalid}")
    private String transactionIsInvalid;



    @Value("${transaction.transaction_has_been_saved_before}")
    private String transactionHasBeenSavedBefore;



    @Value("${transaction.transaction_cannot_be_updated}")
    private String transactionCannotBeUpdated;



    @Value("${transaction.transaction_cannot_be_deleted}")
    private String transactionCannotBeDeleted;

    TransactionModelService(final ModelValidator validator, final TransactionService transactionService,
        final TransactionModelTransformer transformer) {
        this.validator = validator;
        this.transactionService = transactionService;
        this.transformer = transformer;
    }

    List<Transaction> findAll(final TransactionRequestContext context) {
        validateTransactionContext(context.getUserId(), context.getTransactionType());
        TransactionContext transactionContext = createTransactionContext(context.getTransactionType(), context.getUserId());
        List<hu.elte.bm.transactionservice.domain.transaction.Transaction> transactionList = transactionService.findAll(context.getStart(), context.getEnd(), transactionContext);
        LocalDate firstPossibleDay = transactionService.getTheFirstDateOfTheNewPeriod(transactionContext);
        return transactionList.stream()
            .map(transaction -> transformer.transformToTransactionModel(transaction, firstPossibleDay))
            .collect(Collectors.toList());
    }

    TransactionResponse saveTransaction(final TransactionRequestContext context) {
        preValidateSavableTransaction(context);
        TransactionResponse result = createResponseWithDefaultValues(context);
        if (isValid(result.getTransaction())) {
            TransactionContext transactionContext = createTransactionContext(context.getTransactionType(), context.getUserId());
            Optional<hu.elte.bm.transactionservice.domain.transaction.Transaction> savedTransaction = transactionService.save(
                transformer.transformToTransaction(result.getTransaction()), transactionContext);
            updateResponse(savedTransaction, result, transactionHasBeenSaved, transactionHasBeenSavedBefore);
        } else {
            result.setMessage(transactionIsInvalid);
        }
        return result;
    }

    TransactionResponse updateTransaction(final TransactionRequestContext context) {
        preValidateUpdatableCategory(context);
        TransactionResponse result = createResponseWithDefaultValues(context);
        if (isValid(result.getTransaction())) {
            TransactionContext transactionContext = createTransactionContext(context.getTransactionType(), context.getUserId());
            Optional<hu.elte.bm.transactionservice.domain.transaction.Transaction> updatedTransaction = transactionService.update(
                transformer.transformToTransaction(result.getTransaction()), transactionContext);
            updateResponse(updatedTransaction, result, transactionHasBeenUpdated, transactionCannotBeUpdated);
        } else {
            result.setMessage(transactionIsInvalid);
        }
        return result;
    }

    TransactionResponse deleteTransaction(final TransactionRequestContext context) {
        preValidateUpdatableCategory(context);
        TransactionResponse result = createResponseWithDefaultValues(context);
        if (isValid(result.getTransaction())) {
            TransactionContext transactionContext = createTransactionContext(context.getTransactionType(), context.getUserId());
            Optional<hu.elte.bm.transactionservice.domain.transaction.Transaction> deletedTransaction = transactionService.delete(
                transformer.transformToTransaction(result.getTransaction()), transactionContext);
            updateResponse(deletedTransaction, result, transactionHasBeenDeleted, transactionCannotBeDeleted);
        } else {
            result.setMessage(transactionIsInvalid);
        }
        return result;
    }

    private void preValidateSavableTransaction(final TransactionRequestContext context) {
        if (context.getTransaction().getId() != null) {
            throw new IllegalArgumentException(transactionIsInvalid);
        }
        validateTransactionModelFields(context);
    }

    private void preValidateUpdatableCategory(final TransactionRequestContext context) {
        Assert.notNull(context.getTransaction().getId(), transactionIsInvalid);
        validateTransactionModelFields(context);
    }

    private void validateTransactionModelFields(final TransactionRequestContext context) {
        validateTransactionContext(context.getUserId(), context.getTransactionType());
        Assert.notNull(context.getTransaction().getTitle(), transactionIsInvalid);
        Assert.notNull(context.getTransaction().getAmount(), transactionIsInvalid);
        Assert.notNull(context.getTransaction().getCurrency(), transactionIsInvalid);
        Assert.notNull(context.getTransaction().getTransactionType(), transactionIsInvalid);
        Assert.notNull(context.getTransaction().getMainCategory(), transactionIsInvalid);
        Assert.notNull(context.getTransaction().getDate(), transactionIsInvalid);
    }

    private void validateTransactionContext(final Long userId, final TransactionType transactionType) {
        Assert.notNull(userId, userIdCannotBeNull);
        Assert.notNull(transactionType, categoryCannotBeNull);
    }

    private TransactionResponse createResponseWithDefaultValues(final TransactionRequestContext context) {
        Transaction transaction = context.getTransaction();
        TransactionContext transactionContext = createTransactionContext(context.getTransactionType(), context.getUserId());
        LocalDate firstPossibleDay = transactionService.getTheFirstDateOfTheNewPeriod(transactionContext);
        transformer.populateValidationFields(transaction, firstPossibleDay);
        TransactionResponse result = new TransactionResponse();
        result.setTransaction(transaction);
        result.setFirstPossibleDay(firstPossibleDay);
        return result;
    }

    private boolean isValid(final Transaction transaction) {
        boolean compulsoryFields = validateCompulsoryFields(transaction);
        boolean endDate = transaction.getEndDate() == null || validator.validate(transaction.getEndDate(), "End date");
        boolean description = transaction.getDescription() == null || validator.validate(transaction.getDescription(), "Description");
        return compulsoryFields && endDate && description;
    }

    private boolean validateCompulsoryFields(final Transaction transaction) {
        boolean title = validator.validate(transaction.getTitle(), "Title");
        boolean amount = validator.validate(transaction.getAmount(), "Amount");
        boolean currency = validator.validate(transaction.getCurrency(), "Currency");
        boolean type = validator.validate(transaction.getTransactionType(), "Type");
        boolean date = validator.validate(transaction.getDate(), "Date");
        return title && amount && currency && type && date;
    }

    private void updateResponse(final Optional<hu.elte.bm.transactionservice.domain.transaction.Transaction> transaction,
        final TransactionResponse response, final String successMessage, final String unSuccessMessage) {
        if (transaction.isPresent()) {
            response.setTransaction(transformer.transformToTransactionModel(transaction.get(), response.getFirstPossibleDay()));
            response.setSuccessful(true);
            response.setMessage(successMessage);
        } else {
            response.setMessage(unSuccessMessage);
        }
    }

    private TransactionContext createTransactionContext(final TransactionType transactionType, final Long userId) {
        return TransactionContext.builder()
            .withTransactionType(transactionType)
            .withUserId(userId)
            .build();
    }

}
