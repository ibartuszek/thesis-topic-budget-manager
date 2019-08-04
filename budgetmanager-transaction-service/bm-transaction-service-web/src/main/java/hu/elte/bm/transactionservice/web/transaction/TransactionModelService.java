package hu.elte.bm.transactionservice.web.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hu.elte.bm.transactionservice.domain.transaction.Transaction;
import hu.elte.bm.transactionservice.domain.transaction.TransactionService;
import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.web.common.ModelValidator;

@Service
public class TransactionModelService {

    private static final String TRANSACTION_IS_INVALID = "The new transaction is invalid.";
    private static final String TRANSACTION_HAS_BEEN_SAVED = "The transaction has been saved.";
    private static final String TRANSACTION_HAS_BEEN_SAVED_BEFORE = "The transaction has been saved before.";
    private static final String TRANSACTION_HAS_BEEN_UPDATED = "The transaction has been updated.";
    private static final String TRANSACTION_CANNOT_BE_UPDATED = "You cannot update this transaction, because it exists.";
    private static final String TRANSACTION_HAS_BEEN_DELETED = "The transaction has been deleted.";
    private static final String TRANSACTION_CANNOT_BE_DELETED = "You cannot delete this transaction.";

    private final ModelValidator validator;
    private final TransactionService transactionService;
    private final TransactionModelTransformer transformer;

    TransactionModelService(final ModelValidator validator, final TransactionService transactionService,
        final TransactionModelTransformer transformer) {
        this.validator = validator;
        this.transactionService = transactionService;
        this.transformer = transformer;
    }

    List<TransactionModel> findAll(final TransactionModelRequestContext context) {
        List<Transaction> transactionList = context.getEnd() == null
            ? transactionService.findAllTransaction(context.getStart(), context.getTransactionType())
            : transactionService.findAllTransaction(context.getStart(), context.getEnd(), context.getTransactionType());
        LocalDate firstPossibleDay = transactionService.getTheFirstDateOfTheNewPeriod(context.getTransactionType());
        return transactionList.stream()
            .map(transaction -> transformer.transformToTransactionModel(transaction, firstPossibleDay))
            .collect(Collectors.toList());
    }

    TransactionModelResponse saveTransaction(final TransactionModelRequestContext context) {
        preValidateSavableTransaction(context.getTransactionModel());
        TransactionModelResponse result = createResponseWithDefaultValues(context);
        if (isValid(result.getTransactionModel())) {
            Optional<Transaction> savedTransaction = transactionService.save(transformer.transformToTransaction(result.getTransactionModel()));
            updateResponse(savedTransaction, result, TRANSACTION_HAS_BEEN_SAVED, TRANSACTION_HAS_BEEN_SAVED_BEFORE);
        } else {
            result.setMessage(TRANSACTION_IS_INVALID);
        }
        return result;
    }

    TransactionModelResponse updateTransaction(final TransactionModelRequestContext context) {
        preValidateUpdatableCategory(context.getTransactionModel());
        TransactionModelResponse result = createResponseWithDefaultValues(context);
        if (isValid(result.getTransactionModel()) && !isLocked(result.getTransactionModel())) {
            Optional<Transaction> updatedTransaction = transactionService.update(transformer.transformToTransaction(result.getTransactionModel()));
            updateResponse(updatedTransaction, result, TRANSACTION_HAS_BEEN_UPDATED, TRANSACTION_CANNOT_BE_UPDATED);
        } else {
            result.setMessage(TRANSACTION_IS_INVALID);
        }
        return result;
    }

    TransactionModelResponse deleteTransaction(final TransactionModelRequestContext context) {
        preValidateUpdatableCategory(context.getTransactionModel());
        TransactionModelResponse result = createResponseWithDefaultValues(context);
        if (isValid(result.getTransactionModel()) && !isLocked(result.getTransactionModel())) {
            boolean successful = transactionService.delete(transformer.transformToTransaction(result.getTransactionModel()));
            updateResponse(successful, result, TRANSACTION_HAS_BEEN_DELETED, TRANSACTION_CANNOT_BE_DELETED);
        } else {
            result.setMessage(TRANSACTION_IS_INVALID);
        }
        return result;
    }

    private void preValidateSavableTransaction(final TransactionModel transactionModel) {
        if (transactionModel.getId() != null) {
            throw new IllegalArgumentException(TRANSACTION_IS_INVALID);
        }
        validateTransactionModelFields(transactionModel);
    }

    private void preValidateUpdatableCategory(final TransactionModel transactionModel) {
        Assert.notNull(transactionModel.getId(), TRANSACTION_IS_INVALID);
        validateTransactionModelFields(transactionModel);
    }

    private void validateTransactionModelFields(final TransactionModel transactionModel) {
        Assert.notNull(transactionModel.getTitle(), TRANSACTION_IS_INVALID);
        Assert.notNull(transactionModel.getAmount(), TRANSACTION_IS_INVALID);
        Assert.notNull(transactionModel.getCurrency(), TRANSACTION_IS_INVALID);
        Assert.notNull(transactionModel.getTransactionType(), TRANSACTION_IS_INVALID);
        Assert.notNull(transactionModel.getMainCategory(), TRANSACTION_IS_INVALID);
        Assert.notNull(transactionModel.getDate(), TRANSACTION_IS_INVALID);
    }

    private TransactionModelResponse createResponseWithDefaultValues(final TransactionModelRequestContext context) {
        TransactionModel transactionModel = context.getTransactionModel();
        LocalDate firstPossibleDay = transactionService.getTheFirstDateOfTheNewPeriod(context.getTransactionType());
        transformer.populateValidationFields(transactionModel, firstPossibleDay);
        TransactionModelResponse result = new TransactionModelResponse();
        result.setTransactionModel(transactionModel);
        result.setFirstPossibleDay(firstPossibleDay);
        return result;
    }

    private boolean isValid(final TransactionModel transactionModel) {
        boolean title = validator.validate(transactionModel.getTitle(), "Title");
        boolean amount = validator.validate(transactionModel.getAmount(), "Amount");
        boolean currency = validator.validate(transactionModel.getCurrency(), "Currency");
        boolean type = validator.validate(transactionModel.getTransactionType(), "Type");
        boolean date = validator.validate(transactionModel.getDate(), "Date");
        boolean description = transactionModel.getDescription() == null || validator.validate(transactionModel.getDescription(), "Description");
        return title && amount && currency && type && date && description;
    }

    private boolean isLocked(final TransactionModel transactionModel) {
        return transactionModel.isLocked()
            || transactionService.isLockedTransaction(transactionModel.getId(), TransactionType.valueOf(transactionModel.getTransactionType().getValue()));
    }

    private void updateResponse(final Optional<Transaction> transaction,
        final TransactionModelResponse response, final String successMessage, final String unSuccessMessage) {
        if (transaction.isPresent()) {
            response.setTransactionModel(transformer.transformToTransactionModel(transaction.get(), response.getFirstPossibleDay()));
            response.setSuccessful(true);
            response.setMessage(successMessage);
        } else {
            response.setMessage(unSuccessMessage);
        }
    }

    private void updateResponse(final boolean successful,
        final TransactionModelResponse response, final String successMessage, final String unSuccessMessage) {
        if (successful) {
            response.setSuccessful(true);
            response.setMessage(successMessage);
        } else {
            response.setMessage(unSuccessMessage);
        }
    }

}
